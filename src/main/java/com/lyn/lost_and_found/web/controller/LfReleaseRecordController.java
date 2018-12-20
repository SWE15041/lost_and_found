package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfReleaseRecords")
public class LfReleaseRecordController extends BaseLFGridController<LfReleaseRecord, Long> {

    @Autowired
    private LfReleaseRecordService releaseRecordService;

// todo 用户发布物品之后返回推荐标签算法


    /**
     * 发布遗失
     * @param goods
     * @return
     */
    @RequestMapping(value = "/releaseLoss",method = RequestMethod.POST)
    public boolean releaseLoss(@RequestBody LfGoods goods){
        if(Validator.isNull(goods)){
            throw new HttpBadRequestException("物品信息为空，发布失败","RELEASE_FALI");
        }
        boolean releaseGoods = releaseRecordService.releaseGoods(goods, ReleaseType.LOSS);
        return releaseGoods;
    }

    /**
     * 发布拾遗
     * @param goods
     * @return
     */
    @RequestMapping(value = "/releasePickup",method = RequestMethod.POST)
    public boolean releasePickup(@RequestBody LfGoods goods){
        if(Validator.isNull(goods)){
            throw new HttpBadRequestException("物品信息为空，发布失败","RELEASE_FALI");
        }
        boolean releaseGoods = releaseRecordService.releaseGoods(goods, ReleaseType.PICK_UP);
        return releaseGoods;
    }
}
