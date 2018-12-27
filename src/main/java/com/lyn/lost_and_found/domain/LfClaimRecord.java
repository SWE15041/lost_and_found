package com.lyn.lost_and_found.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ClaimRecordType;
import com.lyn.lost_and_found.config.constant.ClaimStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 认领记录实体
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
     * 发布方ID
     */
    private Long releaseUserId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 实际悬赏金额
     */
    private Double money;
    /**
     * 删除
     */
    @Enumerated
    private YesNoNum delState = YesNoNum.no;
    /**
     * 认领者填写的手机号
     */
    private String mobile;
    /**
     * 认领者物品丢失时间
     */
    private Date lossTime;
    /**
     * 认领者物品信息描述
     */
    private String description;
    /**
     * 发布类型 0-拾遗 1-遗失
     */
    @Enumerated
    private ReleaseType releaseType;
    /**
     * 认领记录状态： 0-等待同意 1-认领成功 2-认领失败
     */
    @Enumerated
    private ClaimStatus claimStatus;
    /**
     * 认领记录类型：0-我发出的 1-我收到的
     */
    @Enumerated
    private ClaimRecordType claimRecordType;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss:SSS")
    public Date getLossTime() {
        return lossTime;
    }

    public void setLossTime(Date lossTime) {
        this.lossTime = lossTime;
    }
}

