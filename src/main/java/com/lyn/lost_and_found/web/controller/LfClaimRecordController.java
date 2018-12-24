package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/claimRecords")
public class LfClaimRecordController extends BaseLFGridController<LfClaimRecord, Long> {

    @Autowired
    private LfClaimRecordService claimRecordService;
    @Autowired
    private LfReleaseRecordService releaseRecordService;

    /**
     * 创建认领记录 交易记录
     *
     * @param claimRecord
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Override
    public LfClaimRecord save(@RequestBody LfClaimRecord claimRecord) {
        if (Validator.isNull(claimRecord)) {
            throw new HttpBadRequestException("请填写认领者信息", "INVALID_CLAIM_INFO");
        }

        LfClaimRecord save = null;
        try {
            save = claimRecordService.save(claimRecord);
        } catch (Exception e) {
            throw new HttpBadRequestException(e.getMessage(), "FAIL_TO_CLAIM");
        }
        return save;
    }

    /**
     * 我的认领记录
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "pageNo")
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }


    /**
     * 获取goodsId的所有认领者
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/getByGoodsId/{goodsId}", method = RequestMethod.GET)
    public Object getByGoodsId(@PathVariable("goodsId") Long goodsId) {
        List<LfClaimRecord> claimRecords = claimRecordService.findBygoodsId(goodsId);
        return claimRecords;

    }

    /**
     * 物品认领详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Override
    public LfClaimRecord get(@PathVariable("id") Long id) {
        LfClaimRecord claimRecord = super.get(id);
        return claimRecord;
    }

    /**
     * 同意认领
     *
     * @param claimRecord
     * @return
     */
    @RequestMapping(value = "agreeClaim/{id}", method = RequestMethod.POST)
    public boolean agreeClaim(@RequestBody LfClaimRecord claimRecord) {
        Long claimUserId = claimRecord.getClaimUserId();
        Long goodsId = claimRecord.getGoodsId();
        boolean agreeClaim = claimRecordService.agreeClaim(claimRecord);
        return agreeClaim;
    }

    /**
     * 拒绝认领
     *
     * @param claimRecord
     * @return
     */
    @RequestMapping(value = "/refuseClaim/{id}", method = RequestMethod.POST)
    public boolean refuseClaim(@RequestBody LfClaimRecord claimRecord) {
        Long claimUserId = claimRecord.getClaimUserId();
        Long goodsId = claimRecord.getGoodsId();
        boolean agreeClaim = claimRecordService.refuseClaim(claimRecord);
        return agreeClaim;
    }

    /**
     * 修改认领信息
     *
     * @param id
     * @param claimRecord
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @Override
    public LfClaimRecord update(@PathVariable("id ") Long id, @RequestBody LfClaimRecord claimRecord) {
        claimRecord.setId(id);
        claimRecordService.updateNotNull(claimRecord);
        return claimRecord;
    }

    /**
     * 取消认领
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @Override
    public Boolean delete(@PathVariable("id") Long id) {
        claimRecordService.delete(id);
        return true;
    }
}

