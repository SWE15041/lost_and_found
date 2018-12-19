package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfGoodsRepository;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.domain.LfUserRepository;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LfGoodsServiceImpl extends EntityCRUDServiceImpl<LfGoods, Long> implements LfGoodsService {

    @Autowired
    private LfGoodsRepository lfGoodsRepository;

    @Override
    protected JpaRepository<LfGoods, Long> getRepository() {
        return super.getRepository();
    }
}
