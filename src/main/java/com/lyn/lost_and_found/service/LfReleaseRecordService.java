package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.domain.LfUser;

public interface LfReleaseRecordService extends EntityCRUDService<LfReleaseRecord, Long> {

    /**
     * 发布遗失物
     * @param goods
     * @return
     */
    boolean releaseGoods(LfGoods goods, ReleaseType releaseType);

    LfReleaseRecord getByGoodsId(Long goodsId);

    LfReleaseRecord getByReleaseUserIdAndGoodsId(Long releaseUserId ,Long goodsId);

}
