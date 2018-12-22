package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.domain.LfClaimRecord;
import com.lyn.lost_and_found.service.LfClaimRecordService;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfClaimRecords")
public class LfClaimRecordController extends BaseLFGridController<LfClaimRecord, Long> {

    @Autowired
    private LfClaimRecordService claimRecordService;

    /**
     *  创建认领记录 交易记录
     * @param claimRecord
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Override
    public LfClaimRecord save(@RequestBody LfClaimRecord claimRecord) {
        if (Validator.isNull(claimRecord)) {
            throw new HttpBadRequestException("请填写认领者信息", "INVALID_CLAIM_INFO");
        }

        return claimRecordService.save(claimRecord);
    }

    /**
     * 我的认领记录
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,params = "pageNo")
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }
}
