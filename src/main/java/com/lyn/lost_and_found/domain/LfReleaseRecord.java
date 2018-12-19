package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.IssueType;
import lombok.Data;

@Data
public class LfReleaseRecord extends BaseEntity<Long> {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 物品ID
     */
    private Long goodsId;
    /**
     * 发布类型 :0-拾遗 1-遗失
     */
    private IssueType issueType;

}
