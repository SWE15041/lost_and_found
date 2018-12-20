package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.*;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LfReleaseRecordServiceImpl extends EntityCRUDServiceImpl<LfReleaseRecord, Long> implements LfReleaseRecordService {

    @Autowired
    private LfReleaseRecordRepository lfReleaseRecordRepository;
    @Autowired
    private LfGoodsService goodsService;

    @Override
    protected JpaRepository<LfReleaseRecord, Long> getRepository() {
        return super.getRepository();
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean releaseGoods(LfGoods goods, ReleaseType releaseType) {
        //新增物品记录
        goodsService.save(goods);
        //新增用户发布记录
        Long goodsId = goods.getId();
        LfReleaseRecord releaseRecord=new LfReleaseRecord();
        releaseRecord.setGoodsId(goodsId);
        releaseRecord.setReleaseType(releaseType);
        Long currentUserId = UserContextHolder.getCurrentUserId();
        releaseRecord.setUserId(currentUserId);
        super.save(releaseRecord);
        return true;
    }
}



