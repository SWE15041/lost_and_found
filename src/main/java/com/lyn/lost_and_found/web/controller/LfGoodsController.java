package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.util.bean.BeanUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.lyn.lost_and_found.domain.LfCategory;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.service.LfCategoryService;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.utils.FileUtil;
import com.lyn.lost_and_found.web.vo.LfGoodsVO;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/goods")
public class LfGoodsController extends BaseLFGridController<LfGoods, Long> {

    @Autowired
    private LfGoodsService goodsService;
    @Autowired
    private LfCategoryService categoryService;

    /**
     * 获取物品详细信息
     *
     * @param id 物品ID
     * @return 物品信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Override
    public LfGoodsVO get(@PathVariable("id") Long id) {
        LfGoods lfGoods = super.get(id);
        if (Validator.isNull(lfGoods)) {
            return null;
        }
        Long categoryId = lfGoods.getCategoryId();

        LfCategory category = categoryService.get(categoryId);
        String categoryName = category.getName();

        LfGoodsVO lfGoodsVO = new LfGoodsVO();
        lfGoodsVO.setCategoryName(categoryName);
        BeanUtil.copyProperties(lfGoodsVO, lfGoods);

        return lfGoodsVO;
    }

    /**
     * 分页查询:查找物品
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "pageNo")
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }

    @IgnoreUserAuth
    @RequestMapping(value = "/uploadPicture")
    public Map<String, Object> uploadPicture(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        String picPath = null;
        try {
            picPath = FileUtil.uploadFile(multipartFile);
        } catch (Exception e) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("picPath", picPath);
        return map;
    }

    @IgnoreUserAuth
    @RequestMapping(value = "/getPicture", method = RequestMethod.POST)
    public byte[] getPicture(@RequestBody Map<String, Object> map) {
        String picture = String.valueOf(map.get("picture"));
        byte[] data = FileUtil.get(picture);
        return data;
    }

    public static void main(String[] args) {
        LfGoods goods = new LfGoods();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        simpleDateFormat.format(date);
        System.out.println(date);
        System.out.println(simpleDateFormat.getTimeZone());
        goods.setReleaseTime(date);
        LfGoodsVO goodsVO = new LfGoodsVO();
        BeanUtil.copyProperties(goodsVO, goods);
        System.out.println(goodsVO.getReleaseTime());

    }

}
