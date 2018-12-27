package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ClaimStatus;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.domain.LfClaimRecordRepository;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
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
    private LfGoodsService goodsService;

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
        //生成认领记录 记录状态置为 0-等待同意
        Long releaseUserId = releaseRecord.getReleaseUserId();
        Long claimUserId = UserContextHolder.getCurrentUserId();
        claimRecord.setReleaseUserId(releaseUserId);
        claimRecord.setClaimUserId(claimUserId);
        claimRecord.setClaimStatus(ClaimStatus.WAITING_PERMISSION);
        super.save(claimRecord);

        return claimRecord;
    }

    @Override
    public List<LfClaimRecord> findBygoodsId(Long goodsId) {
        List<LfClaimRecord> claimRecords = claimRecordRepository.findByGoodsId(goodsId);
        return claimRecords;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean agreeClaim(LfClaimRecord claimRecord) {
        //同意认领之后修改 认领状态:1-认领成功
        Long goodsId = claimRecord.getGoodsId();
        Long claimUserId = claimRecord.getClaimUserId();
        LfClaimRecord lfClaimRecord = claimRecordRepository.findByGoodsIdAndClaimUserId(goodsId, claimUserId);
        lfClaimRecord.setClaimStatus(ClaimStatus.AGREE);
        super.update(lfClaimRecord);

        //发布记录状态 1-认领成功
        Long releaseUserId = UserContextHolder.getCurrentUserId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByReleaseUserIdAndGoodsId(releaseUserId, goodsId);
        if (Validator.isNull(releaseRecord)) {
            throw new RuntimeException("同意认领失败");
        }
        releaseRecord.setClaimStatus(ClaimStatus.AGREE);
        releaseRecordService.update(releaseRecord);

        //物品状态： 1-已被认领
        LfGoods goods = goodsService.get(goodsId);
        goods.setStatus(ReleaseStatus.CLAIMED);
        goodsService.update(goods);
        return true;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean refuseClaim(LfClaimRecord claimRecord) {
        //拒绝认领之后修改 认领状态:2-认领失败
        Long goodsId = claimRecord.getGoodsId();
        Long claimUserId = claimRecord.getClaimUserId();
        LfClaimRecord lfClaimRecord = claimRecordRepository.findByGoodsIdAndClaimUserId(goodsId, claimUserId);
        lfClaimRecord.setClaimStatus(ClaimStatus.REFUSE);
        super.updateNotNull(lfClaimRecord);
        //发布记录状态 4-等待认领
        Long releaseUserId = UserContextHolder.getCurrentUserId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByReleaseUserIdAndGoodsId(releaseUserId, goodsId);
        if (Validator.isNull(releaseRecord)) {
            throw new RuntimeException("拒绝认领失败");
        }
        releaseRecord.setClaimStatus(ClaimStatus.WAITING_CLAIM);
        releaseRecordService.updateNotNull(releaseRecord);

        //物品状态： 0-未认领
        LfGoods goods = goodsService.get(goodsId);
        goods.setStatus(ReleaseStatus.UNCLAIM);
        goodsService.update(goods);

        return false;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(Long id) {
        LfClaimRecord claimRecord = super.get(id);
        Long goodsId = claimRecord.getGoodsId();
        LfReleaseRecord releaseRecord = releaseRecordService.getByGoodsId(goodsId);
        releaseRecord.setClaimStatus(ClaimStatus.WAITING_CLAIM);
        releaseRecordService.update(releaseRecord);
        super.delete(id);
    }
}
