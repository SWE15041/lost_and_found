package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *认领记录实体
 */
@Entity
@Table(name = "lf_ claim_record")
@Data
public class LfClaimRecord extends BaseEntity<Long> {

    /**
     * 发布者ID
     */
    private Long releaseUid;
    /**
     * 认领者ID
     */
    private Long fetcgId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 发布记录ID
     */
    private Long releaseId;
    /**
     * 实际悬赏金额
     */
    private Double money;
    /**
     * 认领状态：0-等待同意 1-认领失败 2-认领成功
     */
    private ReleaseStatus status;
    /**
     * 删除
     */
    private YesNoNum delState = YesNoNum.no;
}

