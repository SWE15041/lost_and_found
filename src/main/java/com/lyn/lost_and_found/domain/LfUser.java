package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户实体
 */
@Entity
@Table(name = "lf_user")
@Data
public class LfUser extends BaseEntity<Long> {

    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 等级
     */
    private Integer grade;
    /**
     * 钱包余额
     */
    private Double balance;
    /**
     * 微信用户openid
     */
    private String wechatOpenid;

}
