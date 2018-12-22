package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 发布记录实体
 */
@Entity
@Table(name = "lf_release_record")
@Data
public class LfReleaseRecord extends BaseEntity<Long> {

    /**
     * 用户ID
     */
    private Long releaseUserId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 发布类型 :0-拾遗 1-遗失
     */
    private ReleaseType releaseType;

}
