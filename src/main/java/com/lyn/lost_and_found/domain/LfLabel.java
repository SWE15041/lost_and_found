package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

@Data
public class LfLabel extends BaseEntity<Long> {

    /**
     * 标签
     */
    private String label;
    /**
     * 值
     */
    private String value;
    /**
     * 发布者ID
     */
    private Long userId;
    /**
     * 发布记录ID
     */
    private Long relesedId;
    /**
     * 逻辑删：0-否 1-是
     */
    private YesNoNum yesNoNum = YesNoNum.no;

}
