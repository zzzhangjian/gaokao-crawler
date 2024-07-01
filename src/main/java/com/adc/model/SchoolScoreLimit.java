package com.adc.model;

import lombok.Data;

import java.io.Serializable;

/**
 *  @author zhangjian
 *  @date 2024年06月30日 12:38
 */
@Data
public class SchoolScoreLimit implements Serializable {
    private String provinceId;
    private String schoolId;
    private Integer min;
    private String type;
    private String zslx;
    private String batch;
    private String name;
    private String year;
}
