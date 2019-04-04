package com.lyn.lost_and_found.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfLabelRepository;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.segmentation.fnlp.FNLPUtil;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfLabelService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.tfidf.TFIDFCalculation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LfLabelServiceImpl extends EntityCRUDServiceImpl<LfLabel, Long> implements LfLabelService {

    @Autowired
    private LfLabelRepository labelRepository;
    @Autowired
    private LfGoodsService goodsService;
    @Autowired
    private LfReleaseRecordService releaseRecordService;


    @Override
    protected JpaRepository<LfLabel, Long> getRepository() {
        return super.getRepository();
    }

//    @Transactional(rollbackOn = Exception.class)
//    @Override
//    public List<LfLabel> calTFIDF(LfReleaseRecord releaseRecord) {
//        //1 获取等待匹配物品的描述信息（发布类型为遗失，筛选拾遗类型的物品）
//        Long goodsId = releaseRecord.getGoodsId();
//        LfGoods goods = goodsService.get(goodsId);
//        String description = goods.getDescription();
//        //2 获取需要匹配的所有物品
//        List<LfGoods> pickUpGoods = goodsService.findGoods(ReleaseType.PICK_UP, ReleaseStatus.UNCLAIM);
//        Map<String, Double> cosSimilarityMaps = new HashMap<>();
//        for (LfGoods pickUpGood : pickUpGoods) {
//            Long pickUpGoodId = pickUpGood.getId();
//            System.out.println(pickUpGoodId);
//            String matchDescripInfo = pickUpGood.getDescription();
//            Double cosVector = TFIDFCosSimilarityUtil.calCosineSimilarity(description, matchDescripInfo);
//            if (cosVector >= 0.5 && cosVector <= 1) {
//                cosSimilarityMaps.put(String.valueOf(pickUpGoodId), cosVector);
//            }
//        }
//        //3 取余弦相似度大于0.5的物品
//        List<String> mapByValue = sortMapByValue(cosSimilarityMaps);
//        mapByValue.forEach(System.out::println);
//        int topNum = 5;
//        List<LfLabel> labelList = new ArrayList<>();
//        for (String matchGoodIds : mapByValue) {
//            if (topNum < 0) {
//                break;
//            }
//            topNum--;
//            long matchGoodId = Long.parseLong(matchGoodIds);
//            LfReleaseRecord record = releaseRecordService.getByGoodsId(matchGoodId);
//            LfLabel label = new LfLabel();
//            BeanUtils.copyProperties(record, label);
//            label.setId(null);
//            label.setPassiveGoodsId(matchGoodId);
//            label.setPassiveReleaseId(record.getId());
//            label.setLabel(goodsService.get(matchGoodId).getName());
//            super.save(label);
//            labelList.add(label);
//        }
//        Iterator<Map.Entry<String, Double>> iterator = cosSimilarityMaps.entrySet().iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
//        labelList.forEach(System.out::println);
//        return labelList;
//    }

    /**
     * 按map的值进行降序排序
     *
     * @param map
     * @return
     */
    private static List<String> sortMapByValue(Map<String, Double> map) {
        int size = map.size();
        //通过map.entrySet()将map转换为"1.B.1.e=78"形式的list集合
        List<Map.Entry<String, Double>> list = new ArrayList<>(size);
        list.addAll(map.entrySet());
        List<String> keys = list.stream()
                .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed())
                .map(Map.Entry<String, Double>::getKey)
                .collect(Collectors.toList());
        return keys;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<LfLabel> findLable(LfReleaseRecord releaseRecord, LfGoods goodsA) {
        // 主动匹配的文本的 词频向量《关键词》=《keywords》
        List<String> textAkeywords = Arrays.stream((releaseRecord.getKeywords()).split(",")).map(String::valueOf).collect(Collectors.toList());
        // 主动匹配的文本的 所有分词
//        LfGoods goodsA = goodsService.get(releaseRecord.getGoodsId());
        List<String> wordAllA = FNLPUtil.zhCNSegGetNoun(goodsA.getDescription());
        // 与数据库中的记录作比较，计算、保存余弦相似度
        List<LfReleaseRecord> releaseRecords = releaseRecordService.findByReleaseType(ReleaseType.PICK_UP);
        Long activeReleaseRecordId = releaseRecord.getId();
        List<LfLabel> labelList = new ArrayList<>();
        for (LfReleaseRecord record : releaseRecords) {
            // 被匹配的文本的 词频向量
            String recordKeywords = record.getKeywords();
            if (Validator.isNull(recordKeywords)) {
                continue;
            }
            List<String> textBkeywords = Arrays.stream((recordKeywords).split(",")).map(String::valueOf).collect(Collectors.toList());
            Long passiveGoodsId = record.getGoodsId();
            LfGoods goodsB = goodsService.get(passiveGoodsId);
            // 被匹配的文本的 所有分词结果
            List<String> wordAllB = FNLPUtil.zhCNSegGetNoun(goodsB.getDescription());
            // 计算余弦相似度
            Double cosSimilarity = TFIDFCalculation.calCosSimilarity(wordAllA, wordAllB, textAkeywords, textBkeywords);
            LfLabel label = new LfLabel();
            label.setLabel(StringUtils.join(textBkeywords, ","));
            label.setValue(cosSimilarity);
            label.setPassiveGoodsId(passiveGoodsId);
            Long passiveReleaseRecordId = record.getId();
            label.setPassiveReleaseId(passiveReleaseRecordId);
            label.setActiveReleaseId(activeReleaseRecordId);
            labelList.add(label);
            //            cosSimilarityMap.put(StringUtils.join(wordAllB, ","), cosSimilarity);
        }
        //根据余弦相似度给标签排序
        List<LfLabel> labels = labelList.stream().sorted(Comparator.comparing(LfLabel::getValue).reversed()).collect(Collectors.toList()).subList(0, labelList.size() < 5 ? labelList.size() : 5);
        for (LfLabel label : labels) {
            super.save(label);
        }
        return labels;
    }

    public static void main(String[] args) {
        //这里自定义一个需要排序的map集合
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("1.B.1.a", 45.0);
        map.put("1.B.1.e", 65.0);
        map.put("1.B.1.c", 12.0);
        map.put("1.B.1.b", 15.0);
        map.put("1.B.1.d", 78.0);
        List<String> keys = sortMapByValue(map);
        keys.forEach(System.out::println);
    }

//    public static void main(String[] args) {
//        // 默认情况，TreeMap按key升序排序
//        Map<String, Integer> map = new TreeMap<String, Integer>();
//        map.put("acb1", 5);
//        map.put("bac1", 3);
//        map.put("bca1", 20);
//        map.put("cab1", 80);
//        map.put("cba1", 1);
//        map.put("abc1", 10);
//        map.put("abc2", 12);
//
//        // 降序比较器
//        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1,
//                               Map.Entry<String, Integer> o2) {
//                // TODO Auto-generated method stub
//                return o2.getValue() - o1.getValue();
//            }
//        };
//
//        // map转换成list进行排序
//        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
//
//        // 排序
//        Collections.sort(list, valueComparator);
//
//        // 默认情况下，TreeMap对key进行升序排序
//        System.out.println("------------map按照value降序排序--------------------");
//        for (Map.Entry<String, Integer> entry : list) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
//    }
}