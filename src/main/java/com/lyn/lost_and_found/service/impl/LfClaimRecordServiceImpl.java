package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.RecordStatus;
import com.lyn.lost_and_found.domain.*;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.service.LfTransactionRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LfClaimRecordServiceImpl extends EntityCRUDServiceImpl<LfClaimRecord, Long> implements LfClaimRecordService {

    @Autowired
    private LfClaimRecordRepository claimRecordRepository;
    @Autowired
    private LfReleaseRecordService releaseRecordService;
    @Autowired
    private LfTransactionRecordService transactionRecordService;

    @Override
    protected JpaRepository<LfClaimRecord, Long> getRepository() {
        return super.getRepository();
    }

    @Transactional
    @Override
    public LfClaimRecord save(LfClaimRecord claimRecord) {
        super.save(claimRecord);
        Long goodsId = claimRecord.getGoodsId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByGoodsId(goodsId);
        if(Validator.isNull(releaseRecord)){
            throw  new RuntimeException("物品认领失败");
        }
        //生成交易记录
        Long releaseUserId = releaseRecord.getReleaseUserId();
        Long claimUserId = UserContextHolder.getCurrentUserId();
        LfTransactionRecord transactionRecord=new LfTransactionRecord();
        transactionRecord.setClaimUserId(claimUserId);
        transactionRecord.setReleaseUserId(releaseUserId);
        transactionRecord.setGoodsId(goodsId);
        transactionRecord.setRecordStatus(RecordStatus.WAITING_PERMISSION);
        transactionRecordService.save(transactionRecord);
        return claimRecord;
    }
}
