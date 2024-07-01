package com.adc.crawler;

import com.adc.dao.SchoolDao;
import com.adc.dao.SchoolProvinceScoreDao;
import com.adc.dao.SchoolScoreLimitDao;
import com.adc.model.School;
import com.adc.model.SchoolProvinceScore;
import com.adc.model.SchoolScoreLimit;
import com.adc.utils.Http;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 根据分数推荐学校
 * 2023: 2023年
 * 470: 470分同分数线学校
 * https://static-data.gaokao.cn/www/2.0/samescore3/2023/470.json
 */
public class SchoolScoreLimitCrawler extends AbstractCrawler {

    public static void main(String[] args) {
        new SchoolScoreLimitCrawler().crawl();
    }
    final String url = "https://static-data.gaokao.cn/www/2.0/samescore3/%s/%s.json";
    String province_id = "41";
    int year = 2023;
    int start_score = 450;
    int end_score   = 500;
    int total = 0;
    int score = 470;
    String code = "0000";
    @Override
    protected void doCrawl(SqlSession session) {

        final SchoolScoreLimitDao dao = session.getMapper(SchoolScoreLimitDao.class);

        String response = null;
        String code_str = null;
        try {
            do {
                response = Http.get(String.format(url, year, start_score++));
                code_str =  JSON.parseObject(response).getString("code");
                JSONObject dataObj = JSON.parseObject(response).getJSONObject("data");
                JSONObject provinceObj = dataObj.getJSONObject(province_id); //河南招生学校
                Set<String> keySet = provinceObj.keySet();
                for (String key : keySet) {
                    JSONArray school = provinceObj.getJSONArray(key);
                    List<SchoolScoreLimit> scoreList = parseSchool(school);
                    dao.insertList(scoreList);
                    System.out.println(start_score +" ;抓取结束，共抓取了 " + scoreList.size() + " 个学校信息");
                }
                total = keySet.size();
                Thread.sleep(1000);
            } while (code.equals(code_str));
        } catch (Exception e) {
            System.out.println("抓取失败：" + response);
            e.printStackTrace();
        }
    }

    /**
     * {
     *   "province_id": "44",
     *   "school_id": "1285",
     *    "min": 481,
     *    "type": "物理类",
     *    "zslx": "普通类",
     *    "batch": "本科批",
     *    "name": "广州新华学院"
     * }
     * @param items
     * @return
     */
    private List<SchoolScoreLimit> parseSchool(JSONArray items) {
        final List<JSONObject> jsonList = items.toJavaList(JSONObject.class);

        List<SchoolScoreLimit> schools = new ArrayList<>();
        for (JSONObject obj : jsonList) {
            SchoolScoreLimit ssl = obj.toJavaObject(SchoolScoreLimit.class);
            ssl.setYear(year+"");
            schools.add(ssl);
        }
        return schools;
    }
}
