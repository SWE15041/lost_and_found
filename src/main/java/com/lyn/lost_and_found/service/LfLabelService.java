package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfReleaseRecord;

import java.util.List;

public interface LfLabelService extends EntityCRUDService<LfLabel, Long> {

    /**
     * 获取物品描述信息 利用余弦相似性定理进行相似度计算，将匹配的相关记录记录到标签表中
     *
     * @param releaseRecord
     * @return
     */
//    List<LfLabel> calTFIDF(LfReleaseRecord releaseRecord);

    /**
     * 获取推荐标签（计算余弦相似度）
     *
     * @param releaseRecord
     * @return
     */
    List<LfLabel> findLable(LfReleaseRecord releaseRecord, LfGoods goodsA);

}
