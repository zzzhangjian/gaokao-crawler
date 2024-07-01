package com.adc.crawler;

import com.adc.dao.ProvinceDao;
import com.adc.dao.SchoolDao;
import com.adc.dao.SchoolProvinceScoreCrawlTaskDao;
import com.adc.dao.SchoolProvinceScoreDao;
import com.adc.model.Province;
import com.adc.model.School;
import com.adc.model.SchoolProvinceScore;
import com.adc.model.SchoolProvinceScoreCrawlTask;
import com.adc.utils.Http;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.javassist.compiler.ast.Variable;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class SchoolProvinceScoreCrawler extends AbstractCrawler {

    //https://static-data.gaokao.cn/www/2.0/config/dicprovince/provinceScore.json
    //https://static-data.gaokao.cn/www/2.0/schoolprovincescore/2401/2022/41.json
    /*
     * 学校ID
     * 年
     * 省份ID
     */
    private String url = "https://static-data.gaokao.cn/www/2.0/schoolprovincescore/%s/%s/%s.json";
    private String year = "2021";     //年份
    private String provinceId = "41"; //河南
    private int studentType = 1; //理科
    private final int thread_num = 20;
    /**
     * 手动创建线程池
     */
    private ExecutorService executor = Executors.newFixedThreadPool(thread_num);

    public static void main(String[] args) {
        new SchoolProvinceScoreCrawler().crawl();
    }

    @Override
    protected void doCrawl(SqlSession session) {
//        initTask(session);

        final SchoolProvinceScoreCrawlTaskDao crawlTaskDao = session.getMapper(SchoolProvinceScoreCrawlTaskDao.class);
        final SchoolProvinceScoreDao scoreDao = session.getMapper(SchoolProvinceScoreDao.class);
        int executeTasks = 0;
        int remain;
        do {
            CountDownLatch latch = new CountDownLatch(thread_num);
            List<SchoolProvinceScoreCrawlTask> crawlTasks = crawlTaskDao.selectTask(thread_num);
            remain = crawlTasks.size();
            
            if (remain == 0) {
                break;
            }
            for (SchoolProvinceScoreCrawlTask task : crawlTasks) {
                executor.submit(()-> {
                    try {
                        doSpider(crawlTaskDao, scoreDao, task);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        latch.countDown();
                    }
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            doSpider(crawlTaskDao, scoreDao, crawlTasks);
            executeTasks += remain;
            System.out.println("已执行Task：" + executeTasks);
        } while (remain > 0);
    }
    private void doSpider(SchoolProvinceScoreCrawlTaskDao crawlTaskDao,
                          SchoolProvinceScoreDao scoreDao,
                          SchoolProvinceScoreCrawlTask task
    ){

        List<SchoolProvinceScore> scores = executeTask(task);
        if(scores.size()>0){
            scoreDao.insertList(scores);
        }
        task.setStatus(1);
        crawlTaskDao.update(task);
        System.out.println("调用成功：" + scores.size());
    }


    private List<SchoolProvinceScore> executeTask(SchoolProvinceScoreCrawlTask task) {

        String targetUrl = String.format(url,  task.getSchoolId(), task.getYear(), task.getStudentProvinceId());
        try {
            List<SchoolProvinceScore> scores = new ArrayList<>();
            String response = Http.get(targetUrl);
            JSONObject json = JSON.parseObject(response);
            JSONObject dataObj = json.getJSONObject("data");
            Set<String> keySet = dataObj.keySet();
            for(String type: keySet){
                JSONArray item = dataObj.getJSONObject(type).getJSONArray("item");
                for (JSONObject data : item.toJavaList(JSONObject.class)) {
                    SchoolProvinceScore score = new SchoolProvinceScore();
                    score.setSchoolId(task.getSchoolId());
                    score.setStudentProvinceId(task.getStudentProvinceId());
                    score.setStudentType(data.getInteger("type"));
                    score.setYear(task.getYear());

                    if (!"--".equals(data.getString("max"))) {
                        score.setMaxScore(data.getString("max"));
                    }
                    if (!"--".equals(data.getString("min"))) {
                        score.setMinScore(data.getString("min"));
                    }
                    if (!"--".equals(data.getString("average"))) {
                        score.setAvgScore(data.getString("average"));
                    }
                    if (!"--".equals(data.getString("proscore"))) {
                        score.setProScore(data.getString("proscore"));
                    }

                    score.setMinPosition(data.getString("min_section"));
                    score.setBatchName(data.getString("local_batch_name"));

                    scores.add(score);
                }
            }

            return scores;
        } catch (Exception e) {
            System.out.println("抓取分数失败，可能无数据【请核实】：" + targetUrl);
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

    }

    private void initTask(SqlSession session){
        SchoolProvinceScoreCrawlTaskDao crawlTaskDao = session.getMapper(SchoolProvinceScoreCrawlTaskDao.class);
        SchoolDao schoolDao = session.getMapper(SchoolDao.class);
        ProvinceDao provinceDao = session.getMapper(ProvinceDao.class);
        List<Province> provinces = provinceDao.selectAll();
        List<School> schools = schoolDao.selectAll();
        for(School school :schools){
            for(Province province:provinces){
                for(String year:new String[]{"2023","2022","2021","2020"}){
                    List<SchoolProvinceScoreCrawlTask> tasks = new ArrayList<>();
                    for(int stuType:new int[]{1,2}){
                        SchoolProvinceScoreCrawlTask task = new SchoolProvinceScoreCrawlTask();
                        task.setSchoolId(school.getSchoolId());
                        task.setStudentProvinceId(province.getProvinceId());
                        task.setYear(year);
                        task.setStudentType(stuType);
                        task.setStatus(0);
                        tasks.add(task);
                    }
                    crawlTaskDao.insertList(tasks);
                }
            }
        }
    }
}
