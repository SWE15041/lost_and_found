package com.lyn.lost_and_found.config.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 发布类型：0-拾遗 1-遗失
 */
//@JSONType(serializeEnumAsJavaBean = true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReleaseType  {

    PICK_UP(0,"PICK_UP"),
    LOSS(1,"LOSS");

    Integer code;
    String value;

    ReleaseType(Integer code, String value){
        this.code=code;
        this.value=value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

//    @JsonValue
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    @JsonCreator  //枚举入参注解
//    public static ReleaseType getByCode(int code) {
//        for (ReleaseType sccc : values()) {
//            if (sccc.getCode() == code) {
//                return sccc;
//            }
//        }
//    }
}
