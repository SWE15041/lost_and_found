package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.RecordStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物品类别实体
 */
@Entity
@Table(name = "lf_transaction_record")
@Data
public class LfTransactionRecord extends BaseEntity<Long> {

    /**
     * 发布方ID
     */
    private String releaseUserId;
    /**
     * 认领方ID
     */
    private String claimUserId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 物品名称
     */
    private String name;
    /**
     * 发布类型 0-拾遗 1-遗失
     */
    private ReleaseType releaseType;
    /**
     * 记录状态
     */
    private RecordStatus status;
}
