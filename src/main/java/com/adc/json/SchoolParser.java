package com.adc.json;/**
 * Created on 2024/6/30.
 *
 * @author smit
 * @date 2024-06-30 01:15
 */

import com.adc.crawler.SchoolDetailCrawler;
import com.adc.crawler.SchoolProvinceScoreCrawler;
import com.adc.dao.ProvinceDao;
import com.adc.dao.SchoolDao;
import com.adc.dao.SchoolProvinceScoreCrawlTaskDao;
import com.adc.dao.SchoolProvinceScoreDao;
import com.adc.model.Province;
import com.adc.model.School;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;

import java.io.*;
import java.util.*;

public class SchoolParser extends AbstractParser {
    private String linkage = "linkage.json";
    private String list_v2 = "list_v2.json";
    private String name = "name.json";
    private String school_code = "school_code.json";

    ProvinceDao provinceDao;
    SchoolDao schoolDao;
    SchoolProvinceScoreDao schoolProvinceScoreDao;
    SchoolProvinceScoreCrawlTaskDao schoolProvinceScoreCrawlTaskDao;



    @Override
    protected void doParser(SqlSession session) {
        initSession(session);

        List<School> schoolList = new ArrayList<>();

        // 加载json文件
        String root = getClass().getResource("").getPath();

        // 解析json数据并插入到数据库
        schoolList = parseSchoolId(root + linkage);
//        schoolDao.insertList(schoolList);

        // 更新short_name,old_name
//        String jsonStr = readJsonGBK(root + name);
//        JSONObject nameObj = JSON.parseObject(jsonStr);
//        JSONArray schoolArray = nameObj.getJSONArray("data");
//        for(int i = 0; i < schoolArray.size(); i++) {
//            JSONObject schoolObj = schoolArray.getJSONObject(i);
//
//            School school = new School();
//            school.setSchoolId(schoolObj.getString("school_id"));
//            school.setSchoolName(schoolObj.getString("name"));
//            school.setShortName(schoolObj.getString("short"));
//            school.setOldName(schoolObj.getString("old_name"));
//            school.setType(schoolObj.getString("type"));
//            school.setProvinceId(schoolObj.getString("proid"));
//
//            schoolDao.update(school);
//        }

        // 更新school_code信息
//        String jsonStr = readJsonGBK(root + school_code);
//        JSONObject schoolCodeObj = JSON.parseObject(jsonStr);
//        JSONObject schoolCodeDataObj = schoolCodeObj.getJSONObject("data");
//        Set<String> keySet = schoolCodeDataObj.keySet();
//        for(String code : keySet) {
//            JSONObject sObj = schoolCodeDataObj.getJSONObject(code);
//            School school = new School();
//            school.setSchoolId(sObj.getString("school_id"));
//            school.setSchoolName(sObj.getString("name"));
//            school.setSchoolCode(code);
//
//            schoolDao.update(school);
//        }
          //更新详细信息
//        String jsonStr = readJsonGBK(root + list_v2);
//        JSONObject schoolObj = JSON.parseObject(jsonStr);
//        JSONObject schoolDataObj = schoolObj.getJSONObject("data");
//        Set<String> keySet = schoolDataObj.keySet();
//        for(String school_id : keySet) {
//            JSONObject sObj = schoolDataObj.getJSONObject(school_id);
//            School school = new School();
//            school.setSchoolId(school_id);
//            school.setSchoolName(sObj.getString("name"));
//            school.setF985(sObj.getString("f985"));
//            school.setF211(sObj.getString("f211"));
//            school.setProvinceName(sObj.getString("p"));
//            school.setCityName(sObj.getString("c"));
//            school.setQj(sObj.getString("qj"));
//            school.setIntro(sObj.getString("answerurl"));
//            school.setDualClass(sObj.getString("dual_class"));
//            school.setNature(sObj.getString("nature"));
//            school.setLevel(sObj.getString("level"));
//
//            schoolDao.update(school);
//        }

        // 更新省信息
//        List<School> schools = schoolDao.selectAll();
//        Map<String, String> provinces = new HashMap<>();
//        for(School school: schools){
//            provinces.put(school.getProvinceId(),school.getProvinceName());
//        }
//        List<Province> provinceList = new ArrayList<>();
//        for(Map.Entry<String,String> entry:provinces.entrySet()){
//            Province province = new Province();
//            province.setProvinceId(entry.getKey());
//            province.setProvinceName(entry.getValue());
//            provinceList.add(province);
//        }
//        provinceDao.insertList(provinceList);

    }

    private List<School> parseSchoolId(String jsonFile){
        List<School> schoolList = new ArrayList<>();
        // 解析json数据并插入到数据库
        String jsonStr = readJsonGBK(jsonFile);
        JSONObject linkageObj = JSON.parseObject(jsonStr);
        JSONArray schoolArray = linkageObj.getJSONObject("data").getJSONArray("school");
        for(int i = 0; i < schoolArray.size(); i++) {
            JSONObject schoolObj = schoolArray.getJSONObject(i);
            String school_id = schoolObj.getString("school_id");
            String name = schoolObj.getString("name");
            School school = new School();
            school.setSchoolId(school_id);
            school.setSchoolName(name);
            schoolList.add(school);
        }
        schoolDao.insertList(schoolList);
        return schoolList;
    }

    private void initSession(SqlSession session){
        provinceDao = session.getMapper(ProvinceDao.class);
        schoolDao = session.getMapper(SchoolDao.class);
        schoolProvinceScoreDao = session.getMapper(SchoolProvinceScoreDao.class);
        schoolProvinceScoreCrawlTaskDao = session.getMapper(SchoolProvinceScoreCrawlTaskDao.class);
    }

    /**
     * 读取json文件并且转换成字符串(读取GBK编码方式的)
     * @param jsonFile 文件的路径
     * @return
     * @throws IOException
     */
    private  String readJsonGBK(String jsonFile) {

        StringBuffer sb = new StringBuffer();
        File myFile = new File(jsonFile);//"D:"+File.separatorChar+"DStores.json"
        if (!myFile.exists()) {
            System.err.println("Can't Find " + jsonFile);
        }
        try {
            FileInputStream fis = new FileInputStream(jsonFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);  //new String(str,"UTF-8")
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        new SchoolParser().parser();
    }
}
