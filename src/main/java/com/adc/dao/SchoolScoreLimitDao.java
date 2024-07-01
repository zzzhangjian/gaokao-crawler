package com.adc.dao;

import com.adc.model.SchoolScoreLimit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SchoolScoreLimitDao {
    void insertList(@Param("scoreList") List<SchoolScoreLimit> scoreList);
}
