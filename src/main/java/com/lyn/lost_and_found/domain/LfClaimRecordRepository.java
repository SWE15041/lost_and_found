package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LfClaimRecordRepository extends MyJpaRepository<LfClaimRecord, Long> {

    List<LfClaimRecord> findByGoodsId(Long goodsId);

    LfClaimRecord findByGoodsIdAndClaimUserId(Long goodsId, Long claimUserId);

    List<LfClaimRecord> findByGoodsIdAndClaimUserIdNot(Long goodsId, Long claimUserId);


}
