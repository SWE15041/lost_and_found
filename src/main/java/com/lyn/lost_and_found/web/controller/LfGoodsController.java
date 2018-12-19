package com.lyn.lost_and_found.web.controller;

import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfGoods")
public class LfGoodsController extends BaseLFGridController<LfGoods, Long> {

    @Autowired
    private LfGoodsService lfGoodsService;

    /**
     * 发布拾遗,将用户发布的物品信息新增到物品表
     * @param goods
     * @return true or false
     */
    @RequestMapping(value = "/releasePickup",method = RequestMethod.POST)
    public Map<String,Object> releasePickup(@RequestBody LfGoods goods){
        Long currentUserId = UserContextHolder.getCurrentUserId();
        //todo
        return null;
    }


}
