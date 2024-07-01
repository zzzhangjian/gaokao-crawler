package com.adc.model;

import lombok.Data;

@Data
public class SchoolProvinceScore {
    private String schoolId;
    private String studentProvinceId;
    private Integer studentType;
    private String year;
    private String maxScore;
    private String minScore;
    private String avgScore;
    private String proScore;
    private String minPosition;
    private String batchName;
}
