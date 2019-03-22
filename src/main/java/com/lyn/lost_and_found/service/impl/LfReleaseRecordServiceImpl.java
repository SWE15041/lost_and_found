package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.domain.LfReleaseRecordRepository;
import com.lyn.lost_and_found.segmentation.fnlp.FNLPUtil;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfLabelService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.tfidf.TFIDFCalculation;
import com.lyn.lost_and_found.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LfReleaseRecordServiceImpl extends EntityCRUDServiceImpl<LfReleaseRecord, Long> implements LfReleaseRecordService {

    @Autowired
    private LfReleaseRecordRepository releaseRecordRepository;
    @Autowired
    private LfGoodsService goodsService;
    @Autowired
    private LfLabelService labelService;

    @Override
    protected JpaRepository<LfReleaseRecord, Long> getRepository() {
        return super.getRepository();
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<LfLabel> releaseGoods(LfGoods goods) {
        //新增物品记录
        goods.setReleaseStatus(ReleaseStatus.UNCLAIM);
        goodsService.save(goods);
        //新增用户发布记录
        Long goodsId = goods.getId();
        LfReleaseRecord releaseRecord = new LfReleaseRecord();
        releaseRecord.setGoodsId(goodsId);
        releaseRecord.setReleaseType(goods.getReleaseType());
        Long currentUserId = UserContextHolder.getCurrentUserId();
        releaseRecord.setReleaseUserId(currentUserId);
        releaseRecord.setReleaseStatus(ReleaseStatus.UNCLAIM);
//        super.save(releaseRecord);

        //给物品描述进行 分词、计算关键词：默认取5个
        String description = goods.getDescription();
        if (Validator.isNotNull(description)) {
            List<String> wordAll = FNLPUtil.zhCNSeg(description);
            Map<String, Double> tfidfs = TFIDFCalculation.calTFIDF(wordAll);
            List<Map.Entry<String, Double>> entryList = tfidfs.entrySet().stream().
                    sorted(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed()).
                    collect(Collectors.toList());
            List<Map.Entry<String, Double>> topEntryList = entryList.subList(0, entryList.size() < 5 ? entryList.size() : 5);
            Map<String, Double> topEntryListToMap = topEntryList.stream().collect(Collectors.toMap(m -> m.getKey(), m -> m.getValue()));
            // 把数据以字符串的形式保存到数据库
            String keywords = StringUtils.join(new ArrayList(topEntryListToMap.keySet()), ",");
            releaseRecord.setKeywords(keywords);
            String values = StringUtils.join(topEntryListToMap.values().toArray(), ",");
            releaseRecord.setTfidfs(values);
        }
        super.save(releaseRecord);

        //推荐 给发布遗失的用户做推荐
        List<LfLabel> labelList = new ArrayList<>();
        if (goods.getReleaseType().equals(ReleaseType.LOSS)) {
            labelList = labelService.findLable(releaseRecord);
        }
        return labelList;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean updateReleaseInfo(Long id, LfGoods goods) {

        LfReleaseRecord releaseRecord = super.get(id);
        Long goodsId = releaseRecord.getGoodsId();
        //todo 1.删除原来存储在服务端磁盘的图片
        LfGoods oldGoods = goodsService.get(goodsId);
        if (Validator.isNotNull(oldGoods)) {
            String picture = oldGoods.getPicture();
            boolean delete = FileUtil.delete(picture);
        }
        //2.修改物品表信息
        goods.setId(goodsId);
        goods.setReleaseStatus(ReleaseStatus.UNCLAIM);
        goodsService.updateNotNull(goods);
        return true;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(Long id) {
        LfReleaseRecord releaseRecord = super.get(id);
        Long goodsId = releaseRecord.getGoodsId();
        LfGoods goods = goodsService.get(goodsId);
        //todo 删除服务端磁盘的图片文件
        String picture = goods.getPicture();
        if (Validator.isNotNull(picture)) {
            boolean delete = FileUtil.delete(picture);
        }
        goodsService.delete(goodsId);
        super.delete(id);
    }

    @Override
    public LfReleaseRecord getByGoodsId(Long goodsId) {
        LfReleaseRecord releaseRecord = releaseRecordRepository.findByGoodsId(goodsId);
        return releaseRecord;
    }

    @Override
    public LfReleaseRecord getByReleaseUserIdAndGoodsId(Long releaseUserId, Long goodsId) {
        LfReleaseRecord releaseRecord = releaseRecordRepository.findByGoodsIdAndReleaseUserId(goodsId, releaseUserId);
        return releaseRecord;
    }

    @Override
    public LfReleaseRecord findByGoodsId(Long goodsId) {
        return findByGoodsId(goodsId);
    }

    @Override
    public List<LfReleaseRecord> findByReleaseType(ReleaseType releaseType) {
        return releaseRecordRepository.findByReleaseType(releaseType);
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(i);
        }
        List<Integer> sub = list.subList(0, list.size() < 10 ? list.size() : 10);
        System.out.println(sub);

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        List<Map<String, Integer>> list1 = new ArrayList<>();
        list1.add(map);
        Map<Set<String>, Collection<Integer>> collect = list1.stream().collect(Collectors.toMap(m -> m.keySet(), m -> m.values()));
        System.out.println(collect);
//        String[] ss=new String[collect.size()];
        List<String> ss = new ArrayList<>();
        String s = StringUtils.join(map.values().toArray(), "!");
        System.out.println(s);
    }
}



