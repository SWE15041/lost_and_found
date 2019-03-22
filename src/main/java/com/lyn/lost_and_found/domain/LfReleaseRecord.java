package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.List;

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

    /**
     * 记录状态：0-等待同意 1-同意认领 2-拒绝认领  3-未被认领
     */
    private ReleaseStatus releaseStatus;

    /**
     * 一组关键词，默认10个
     */
    private String keywords;

    /**
     * 一组关键词对应的tfidf值
     */
    private String tfidfs;

    @Enumerated(EnumType.ORDINAL)
    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }

    @Enumerated(EnumType.ORDINAL)
    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }
}
