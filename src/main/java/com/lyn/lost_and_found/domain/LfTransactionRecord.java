package com.lyn.lost_and_found.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.GoodsStatus;
import com.lyn.lost_and_found.config.constant.RecordStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 交易记录：包括发布者和认领者到相关信息
 */
@Entity
@Table(name = "lf_transaction_record")
@Data
public class LfTransactionRecord extends BaseEntity<Long> {

    /**
     * 发布方ID
     */
    private Long releaseUserId;
    /**
     * 认领方ID
     */
    private Long claimUserId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 发布类型 0-拾遗 1-遗失
     */
    private ReleaseType releaseType;
    /**
     * 交易状态：0-等待同意 1-交易成功 2-交易失败 3-交易进行中
     */
    private RecordStatus recordStatus;

}
