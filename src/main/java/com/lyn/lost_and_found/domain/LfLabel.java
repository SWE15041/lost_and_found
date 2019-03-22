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
     * cos余弦相似度
     */
    private Double value;
    /**
     * 被匹配的发布者ID
     */
    private Long passiveReleaseUserId;
    /**
     * 被匹配的发布记录ID
     */
    private Long passiveReleaseId;
    /**
     * 被匹配的物品id
     */
    private Long passiveGoodsId;

    /**
     * 主动匹配的 发布记录ID
     */
    private Long activeReleaseId;
    /**
     * 逻辑删：0-否 1-是
     */
    private YesNoNum delState = YesNoNum.no;
}