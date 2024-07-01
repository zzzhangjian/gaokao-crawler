package com.adc.model;

import lombok.Data;

@Data
public class School {
    private String schoolId;
    private String schoolName;
    private String schoolCode;
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
    private String level;
    private String type;
    private String nature;
    private String email;
    private String phone;
    private String site;
    private String address;
    private String intro;
    private Integer status;

    private String shortName;
    private String oldName;
    private String f985;
    private String f211;
    private String qj;
    private String dualClass;

}
