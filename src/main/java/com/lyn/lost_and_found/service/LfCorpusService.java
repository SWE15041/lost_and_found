package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.domain.LfCorpus;
import com.lyn.lost_and_found.domain.LfUser;

public interface LfCorpusService extends EntityCRUDService<LfCorpus, Long> {

    /**
     * 获取语料库单词总数
     * @return
     */
    Long getWordSum();

    /**
     * 获取指定单词出现的词数
     * @param name 单词名称
     * @return
     */
    Long getWordQuantities(String name);
}
