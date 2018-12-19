package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import com.lyn.lost_and_found.config.constant.YesNoNum;
import lombok.Data;

@Data
public class LfClass extends BaseEntity<Long> {

    /**
     * 类别名称
     */
    private String name;

}
