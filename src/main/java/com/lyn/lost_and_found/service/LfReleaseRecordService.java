package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfReleaseRecord;

import java.util.List;

public interface LfReleaseRecordService extends EntityCRUDService<LfReleaseRecord, Long> {

    /**
     * 发布遗失物
     *
     * @param goods
     * @return
     */
    List<LfLabel> releaseGoods(LfGoods goods);

    /**
     * 修改发布信息
     *
     * @param goods
     * @return
     */
    boolean updateReleaseInfo(Long id, LfGoods goods);

    LfReleaseRecord getByGoodsId(Long goodsId);

    LfReleaseRecord getByReleaseUserIdAndGoodsId(Long releaseUserId, Long goodsId);

    /**
     *  通过物品id获取发布记录
     * @param goodsId
     * @return
     */
    LfReleaseRecord findByGoodsId(Long goodsId);

    List<LfReleaseRecord> findByReleaseType(ReleaseType releaseType);
}
