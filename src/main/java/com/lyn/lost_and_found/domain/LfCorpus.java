package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物品类别实体
 */
@Entity
@Table(name = "lf_corpus")
@Data
public class LfCorpus extends BaseEntity<Long> {

    /**
     * 单词名称
     */
    private String name;

    /**
     * 出现的次数
     */
    private Long quantities;
}
