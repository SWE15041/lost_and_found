package com.lyn.lost_and_found.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LfGoodsRepository extends MyJpaRepository<LfGoods, Long> {

    List<LfGoods> findByReleaseTypeAndReleaseStatus(ReleaseType releaseType, ReleaseStatus releaseStatus);
}
