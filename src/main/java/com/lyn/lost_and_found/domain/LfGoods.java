package com.lyn.lost_and_found.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 物品实体
 */
@Entity
@Table(name = "lf_goods")
@Data
public class LfGoods extends BaseEntity<Long> {

    /**
     * 物品名称
     */
    private String name;
    /**
     * 物品类别ID
     */
    private Long categoryId;
    /**
     * 预设悬赏金额
     */
    private Double money;
    /**
     * 物品描述
     */
    private String description;
    /**
     * 物品图片
     */
    private String picture;
    /**
     * 图片数量
     */
    private Integer picNum;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 遗失地址
     */
    private  String addr;
    /**
     * 物品状态：0-未认领 1-已认领
     */
    @Enumerated
    private ReleaseStatus releaseStatus = ReleaseStatus.UNCLAIM;
    /**
     * 删除
     */
    private YesNoNum delState = YesNoNum.no;
    /**
     * 发布时间：遗失时间 拾遗时间
     */
    private Date releaseTime;
    /**
     * 发布类型 :0-拾遗 1-遗失
     */
    @Enumerated
    private ReleaseType releaseType;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss:SSS")
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
}

