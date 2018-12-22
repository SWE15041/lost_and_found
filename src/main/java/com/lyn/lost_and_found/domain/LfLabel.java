package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 标签实体
 */
@Entity
@Table(name = "lf_label")
@Data
public class LfLabel extends BaseEntity<Long> {

    /**
     * 标签
     */
    private String label;
    /**
     * 值
     */
    private String value;
    /**
     * 发布者ID
     */
    private Long releaseUserId;
    /**
     * 发布记录ID
     */
    private Long relesedId;
    /**
     * 逻辑删：0-否 1-是
     */
    private YesNoNum yesNoNum = YesNoNum.no;

}
