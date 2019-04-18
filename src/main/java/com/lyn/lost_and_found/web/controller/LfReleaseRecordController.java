package com.lyn.lost_and_found.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.web.vo.LocalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/releaseRecords")
public class LfReleaseRecordController extends BaseLFGridController<LfReleaseRecord, Long> {

    @Autowired
    private LfReleaseRecordService releaseRecordService;

    // todo 用户发布物品之后返回推荐标签算法

    //todo 将发布类型加入到请求参数，将 发布遗失、发布拾遗 两个端口合并？

    /**
     * 发布遗失 或 拾遗
     *
     * @param goods
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public List<LfLabel> save(@RequestBody LfGoods goods) {
        if (Validator.isNull(goods)) {
            throw new HttpBadRequestException("物品信息为空，发布失败", "RELEASE_FALI");
        }
        ReleaseType releaseType = goods.getReleaseType();
        if (Validator.isNull(releaseType)) {
            throw new HttpBadRequestException("无指定发布类型，发布失败", "RELEASE_FALI");
        }
        return releaseRecordService.releaseGoods(goods);
    }

    /**
     * 修改发布遗失 或 拾遗
     *
     * @param goods
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean update(@PathVariable("id") Long id, @RequestBody LfGoods goods) {
        if (Validator.isNull(goods)) {
            throw new HttpBadRequestException("物品信息为空，发布失败", "RELEASE_FALI");
        }
        ReleaseType releaseType = goods.getReleaseType();
        if (Validator.isNull(releaseType)) {
            throw new HttpBadRequestException("无指定发布类型，发布失败", "RELEASE_FALI");
        }
        return releaseRecordService.updateReleaseInfo(id, goods);
    }


    /**
     * 我的发布记录
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "pageNo")
    @Override
    public LocalPage localQuery() {
        return super.localQuery();
    }


    /**
     * 删除发布记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Override
    public Boolean delete(@PathVariable("id") Long id) {
        releaseRecordService.delete(id);
        return true;
    }

    /**
     * 用于算法测试
     * 指定数量发布物品记录
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/buildReleaseData", method = RequestMethod.POST)
    public Boolean releaseGoods(@RequestBody Map<String, String> map) {
        //需要发布的物品记录；物品类型为0；
        Long goodsNum = Long.parseLong(map.get("goodsNum"));
        //测试数据存放的地址
        String corpusDir = map.get("corpusDir");
        //读取生语料库目录
        return releaseRecordService.buildReleaseData(goodsNum, corpusDir);
    }

    /**
     * 用于算法测试
     * 用于测试关键词的模糊查询匹配到的记录数
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/cntReourdNum", method = RequestMethod.POST)
    public Boolean findRecordNumByStr(@RequestBody Map<String, String> map) {
        String keywords = map.get("keywords");
        return releaseRecordService.cntRecords(keywords);
    }

    public static void main(String[] args) {
        String keywords = "[手包,钱包,钥匙,0,9]";
        int indexOf = keywords.indexOf("[");
        int lastIndexOf = keywords.lastIndexOf("]");
        String words = keywords.substring(indexOf + 1, lastIndexOf);
        System.out.println(words);
    }
}
