package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.RecordStatus;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.domain.LfClaimRecordRepository;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.service.LfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LfClaimRecordServiceImpl extends EntityCRUDServiceImpl<LfClaimRecord, Long> implements LfClaimRecordService {

    @Autowired
    private LfClaimRecordRepository claimRecordRepository;
    @Autowired
    private LfReleaseRecordService releaseRecordService;
    @Autowired
    private LfUserService userService;

    @Override
    protected JpaRepository<LfClaimRecord, Long> getRepository() {
        return super.getRepository();
    }

    @Transactional
    @Override
    public LfClaimRecord save(LfClaimRecord claimRecord) {
        Long goodsId = claimRecord.getGoodsId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByGoodsId(goodsId);
        if (Validator.isNull(releaseRecord)) {
            throw new RuntimeException("物品认领失败");
        }
        //生成认领记录 记录状态置为0-等待同意
        Long releaseUserId = releaseRecord.getReleaseUserId();
        Long claimUserId = UserContextHolder.getCurrentUserId();
        claimRecord.setReleaseUserId(releaseUserId);
        claimRecord.setClaimUserId(claimUserId);
        claimRecord.setRecordStatus(RecordStatus.WAITING_PERMISSION);
        super.save(claimRecord);
        return claimRecord;
    }

    @Override
    public List<LfClaimRecord> findBygoodsId(Long goodsId) {
        List<LfClaimRecord> claimRecords = claimRecordRepository.findByGoodsId(goodsId);
        return claimRecords;
    }

    @Override
    public boolean agreeClaim(LfClaimRecord claimRecord) {
        //同意认领之后修改 认领状态 和 发布记录状态
        Long goodsId = claimRecord.getGoodsId();
        Long claimUserId = claimRecord.getClaimUserId();
        LfClaimRecord lfClaimRecord = claimRecordRepository.findByGoodsIdAndClaimUserId(goodsId, claimUserId);
        lfClaimRecord.setRecordStatus(RecordStatus.SUCCESS);
        super.updateNotNull(lfClaimRecord);

        Long releaseUserId = UserContextHolder.getCurrentUserId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByReleaseUserIdAndGoodsId(releaseUserId, goodsId);
        if (Validator.isNull(releaseRecord)) {
            throw new RuntimeException("同意认领失败");
        }
        releaseRecord.setRecordStatus(RecordStatus.SUCCESS);
        releaseRecordService.updateNotNull(releaseRecord);
        return false;
    }

    @Override
    public boolean refuseClaim(LfClaimRecord claimRecord) {
        Long goodsId = claimRecord.getGoodsId();
        Long claimUserId = claimRecord.getClaimUserId();
        LfClaimRecord lfClaimRecord = claimRecordRepository.findByGoodsIdAndClaimUserId(goodsId, claimUserId);
        lfClaimRecord.setRecordStatus(RecordStatus.FAIL);
        super.updateNotNull(lfClaimRecord);

        Long releaseUserId = UserContextHolder.getCurrentUserId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByReleaseUserIdAndGoodsId(releaseUserId, goodsId);
        if (Validator.isNull(releaseRecord)) {
            throw new RuntimeException("拒绝认领失败");
        }
        releaseRecord.setRecordStatus(RecordStatus.WAITING_CLAIM);
        releaseRecordService.updateNotNull(releaseRecord);
        return false;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(Long id) {
        LfClaimRecord claimRecord = super.get(id);
        Long goodsId = claimRecord.getGoodsId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByGoodsId(goodsId);
        releaseRecord.setRecordStatus(RecordStatus.WAITING_CLAIM);
        releaseRecordService.update(releaseRecord);
        super.delete(id);
    }
}
