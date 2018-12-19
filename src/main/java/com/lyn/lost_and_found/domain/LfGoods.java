package com.lyn.lost_and_found.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.GoodsStatus;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

import javax.persistence.Entity;
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
     * 物品状态：0-未认领 1-已认领 2-认领中
     */
    private GoodsStatus status = GoodsStatus.UNCLAIM;
    /**
     * 删除
     */
    private YesNoNum delState = YesNoNum.no;
    /**
     * 遗失时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mi:ss")
    private Date lossTime;
}

