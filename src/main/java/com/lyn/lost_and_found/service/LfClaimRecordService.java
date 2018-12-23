package com.lyn.lost_and_found.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.lyn.lost_and_found.domain.LfClaimRecord;

import java.util.List;

public interface LfClaimRecordService extends EntityCRUDService<LfClaimRecord, Long> {

    /**
     * 通过goodsId获取对应的认领记录
     * @param goodsId
     * @return
     */
    List<LfClaimRecord> findBygoodsId(Long goodsId);

    boolean  agreeClaim(LfClaimRecord claimRecord);

    boolean refuseClaim(LfClaimRecord claimRecord);
}
