package com.lyn.lost_and_found.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ClaimStatus;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 *认领记录实体
 */
@Entity
@Table(name = "lf_claim_record")
@Data
public class LfClaimRecord extends BaseEntity<Long> {

    /**
     * 认领者ID
     */
    private Long claimUserId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 实际悬赏金额
     */
    private Double money;
    /**
     * 认领状态：0-等待同意 1-认领失败 2-认领成功
     */
//    private ClaimStatus status;
    /**
     * 删除
     */
    private YesNoNum delState = YesNoNum.no;
    /**
     * 认领者填写的手机号
     */
    private String mobile;
    /**
     * 认领者邮箱
     */
    private String email;
    /**
     * 认领者物品丢失时间
     */
    @JSONField(format = "yyyy-MM-dd hh:mi:ss")
    private Date lostTime;
    /**
     * 认领者物品信息描述
     */
    private String goodsInfo;
}

