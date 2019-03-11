package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfGoodsRepository;
import com.lyn.lost_and_found.service.LfGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LfGoodsServiceImpl extends EntityCRUDServiceImpl<LfGoods, Long> implements LfGoodsService {

    @Autowired
    private LfGoodsRepository goodsRepository;

    @Override
    protected JpaRepository<LfGoods, Long> getRepository() {
        return super.getRepository();
    }

    @Override
    public List<LfGoods> findGoods(ReleaseType releaseType, ReleaseStatus releaseStatus) {
        return goodsRepository.findByReleaseTypeAndReleaseStatus(releaseType, releaseStatus);
    }
}
