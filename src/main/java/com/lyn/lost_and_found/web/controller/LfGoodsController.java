package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.util.bean.BeanUtil;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.website.web.controller.BaseGridController;
import com.lyn.lost_and_found.domain.LfCategory;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfUser;
import com.lyn.lost_and_found.service.LfCategoryService;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfLabelService;
import com.lyn.lost_and_found.web.vo.LfGoodsVO;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/lost_and_found/lfGoods")
public class LfGoodsController extends BaseLFGridController<LfGoods, Long> {

    @Autowired
    private LfGoodsService lfGoodsService;
    @Autowired
    private LfCategoryService categoryService;

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

    /**
     * 获取物品详细信息
     * @param id 物品ID
     * @return  物品信息
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @Override
    public LfGoodsVO get(@PathVariable("id") Long id){
        LfGoods lfGoods = super.get(id);
        Long categoryId = lfGoods.getCategoryId();

        LfCategory category = categoryService.get(id);
        String categoryName = category.getName();

        LfGoodsVO lfGoodsVO=new LfGoodsVO();
        lfGoodsVO.setCategoryName(categoryName);
        BeanUtil.copyNotNullProperties(lfGoodsVO,lfGoods);

        return lfGoodsVO;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,params = "pageNo")
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }

}
