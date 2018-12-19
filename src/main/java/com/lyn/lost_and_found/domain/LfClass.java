package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物品类别实体
 */
@Entity
@Table(name = "lf_class")
@Data
public class LfClass extends BaseEntity<Long> {

    /**
     * 类别名称
     */
    private String name;

}
