package com.lyn.lost_and_found.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.lyn.lost_and_found.config.constant.ReleaseStatus;
import com.lyn.lost_and_found.config.constant.ReleaseType;
import com.lyn.lost_and_found.domain.LfGoods;
import com.lyn.lost_and_found.domain.LfLabel;
import com.lyn.lost_and_found.domain.LfLabelRepository;
import com.lyn.lost_and_found.domain.LfReleaseRecord;
import com.lyn.lost_and_found.service.LfGoodsService;
import com.lyn.lost_and_found.service.LfLabelService;
import com.lyn.lost_and_found.service.LfReleaseRecordService;
import com.lyn.lost_and_found.tfidf.TFIDFCosSimilarityUtil;
import org.springframework.beans.BeanUtils;
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

    private Map<String, Double> cosSimilarityMaps = new HashMap<>();

    @Override
    protected JpaRepository<LfLabel, Long> getRepository() {
        return super.getRepository();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<LfLabel> calTFIDF(LfReleaseRecord releaseRecord) {
        //1 获取等待匹配物品的描述信息（发布类型为遗失，筛选拾遗类型的物品）
        Long goodsId = releaseRecord.getGoodsId();
        LfGoods goods = goodsService.get(goodsId);
        String description = goods.getDescription();
        //2 获取需要匹配的所有物品
        List<LfGoods> pickUpGoods = goodsService.findGoods(ReleaseType.PICK_UP, ReleaseStatus.UNCLAIM);
        for (LfGoods pickUpGood : pickUpGoods) {
            Long pickUpGoodId = pickUpGood.getId();
            System.out.println(pickUpGoodId);
            String matchDescripInfo = pickUpGood.getDescription();
            Double cosVector = TFIDFCosSimilarityUtil.calCosineSimilarity(description, matchDescripInfo);
            if (cosVector >= 0.5 && cosVector <= 1) {
                cosSimilarityMaps.put(String.valueOf(pickUpGoodId), cosVector);
            }
        }
        //3 取余弦相似度大于0.5的物品
        List<String> mapByValue = sortMapByValue(cosSimilarityMaps);
        mapByValue.forEach(System.out::println);
        int topNum = 5;
        List<LfLabel> labelList = new ArrayList<>();
        for (String matchGoodIds : mapByValue) {
            if (topNum < 0) {
                break;
            }
            topNum--;
            long matchGoodId = Long.parseLong(matchGoodIds);
            LfReleaseRecord record = releaseRecordService.getByGoodsId(matchGoodId);
            LfLabel label = new LfLabel();
            BeanUtils.copyProperties(record, label);
            label.setId(null);
            label.setGoodsId(matchGoodId);
            label.setRelesedId(record.getId());
            label.setLabel(goodsService.get(matchGoodId).getName());
            super.save(label);
            labelList.add(label);
        }
        Iterator<Map.Entry<String, Double>> iterator = cosSimilarityMaps.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        labelList.forEach(System.out::println);
        return labelList;
    }

    /**
     * 按map的值进行降序排序
     *
     * @param map
     * @return
     */
    static List<String> sortMapByValue(Map<String, Double> map) {
        int size = map.size();
        //通过map.entrySet()将map转换为"1.B.1.e=78"形式的list集合
        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(size);
        list.addAll(map.entrySet());
        List<String> keys = list.stream()
                .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed())
                .map(Map.Entry<String, Double>::getKey)
                .collect(Collectors.toList());
        return keys;
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