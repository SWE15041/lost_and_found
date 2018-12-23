package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/releaseRecords")
public class LfReleaseRecordController extends BaseLFGridController<LfReleaseRecord, Long> {

    @Autowired
    private LfReleaseRecordService releaseRecordService;

// todo 用户发布物品之后返回推荐标签算法

    //todo 将发布类型加入到请求参数，将 发布遗失、发布拾遗 两个端口合并？
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

    /**
     * 我的发布记录
     * @return
     */
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }
}
