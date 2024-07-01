package com.adc.crawler;

import com.adc.dao.ProvinceDao;
import com.adc.dao.SchoolDao;
import com.adc.model.Province;
import com.adc.model.School;
import com.adc.utils.Http;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProvinceCrawler extends AbstractCrawler{

    public void doCrawl(SqlSession session) {
        final ProvinceDao provinceDao = session.getMapper(ProvinceDao.class);
        final SchoolDao schoolDao = session.getMapper(SchoolDao.class);
        List<School> schools = schoolDao.selectAll();
       // schools 按照province_id分组
        Set<Province> provinces = schools.stream()
                .filter(s->StringUtils.isNotEmpty(s.getProvinceId()))
                .map(s -> {
                    Province province = new Province();
                    province.setProvinceId(s.getProvinceId());
                    province.setProvinceName(s.getProvinceName());
                    return province;
                }).collect(Collectors.toSet());

        provinces.stream().forEach(s->
                provinceDao.insert(s)
        );

//        String response = Http.get("https://static-data.gaokao.cn/interface/gaokao.eol.cn/pc_fsx.shtml");
//        Document doc = Jsoup.parse(response);
//
//        final Elements elements = doc.select("select.provincelocal>option");
//
//        final ProvinceDao provinceDao = session.getMapper(ProvinceDao.class);
//
//        elements.forEach(element -> {
//            if (StringUtils.isNotBlank(element.val())) {
//                Province province = new Province();
//                province.setProvinceId(element.val());
//                province.setProvinceName(element.text());
//                provinceDao.insert(province);
//            }
//        });
    }

    public static void main(String[] args) {
        new ProvinceCrawler().crawl();
    }
}
