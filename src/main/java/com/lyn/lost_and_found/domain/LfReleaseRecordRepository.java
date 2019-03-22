package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LfReleaseRecordRepository extends MyJpaRepository<LfReleaseRecord, Long> {

    LfReleaseRecord findByGoodsId(Long goodsId);

    LfReleaseRecord findByGoodsIdAndReleaseUserId(Long goodsId, Long releaseUserId);

    List<LfReleaseRecord> findByReleaseType(ReleaseType releaseType);
}
