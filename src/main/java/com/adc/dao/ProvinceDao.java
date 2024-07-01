package com.adc.dao;

import com.adc.model.Province;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProvinceDao {

    void insert(Province province);

    List<Province> selectAll();

    void insertList(@Param("provinceList")List<Province> provinceList);
}
