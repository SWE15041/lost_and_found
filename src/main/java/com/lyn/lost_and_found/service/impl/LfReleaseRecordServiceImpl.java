package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.GoodsStatus;
import com.lyn.lost_and_found.config.constant.RecordStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.domain.LfReleaseRecordRepository;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LfReleaseRecordServiceImpl extends EntityCRUDServiceImpl<LfReleaseRecord, Long> implements LfReleaseRecordService {

    @Autowired
    private LfReleaseRecordRepository releaseRecordRepository;
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
        goods.setStatus(GoodsStatus.UNCLAIM);
        goodsService.save(goods);
        //新增用户发布记录
        Long goodsId = goods.getId();
        LfReleaseRecord releaseRecord = new LfReleaseRecord();
        releaseRecord.setGoodsId(goodsId);
        releaseRecord.setReleaseType(releaseType);
        Long currentUserId = UserContextHolder.getCurrentUserId();
        releaseRecord.setReleaseUserId(currentUserId);
        releaseRecord.setRecordStatus(RecordStatus.WAITING_CLAIM);
        super.save(releaseRecord);
        return true;
    }

    @Override
    public LfReleaseRecord getByGoodsId(Long goodsId) {
        LfReleaseRecord releaseRecord = releaseRecordRepository.findByGoodsId(goodsId);
        return releaseRecord;
    }

    @Override
    public LfReleaseRecord getByReleaseUserIdAndGoodsId(Long releaseUserId, Long goodsId) {
        LfReleaseRecord releaseRecord = releaseRecordRepository.findByGoodsIdAndReleaseUserId(goodsId, releaseUserId);
        return releaseRecord;
    }
}



