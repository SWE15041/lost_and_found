package com.lyn.lost_and_found.tfidf;

import com.jay.vito.common.util.MathUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.website.core.Application;
import com.lyn.lost_and_found.service.LfCorpusService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 将分词独立开的TF/IDF/TF-IDF/余弦相似度的计算；
 */
public class TFIDFCalculation {


    /**
     * 计算文本中每个分词的词频TF;
     * TF（词频） = 某个词在文章中出现的次数；
     * = 某个词在文章中出现的次数 / 该文章的总词数；(当前使用这个)
     * = 某个词在文章中出现的次数 / 该文出现次数最多的词的出现次数；
     *
     * @param wordAll
     * @return
     */
    private static Map<String, Double> calTF(List<String> wordAll) {
        //存放<单词，单词数量>
        Map<String, Integer> dict = new HashMap<>();
        //存放（单词，单词词频）
        Map<String, Double> tfs = new HashMap<>();
        //单词数
        Integer wordCnt = 0;
        for (String word : wordAll) {
            wordCnt++;
            if (dict.containsKey(word)) {
                dict.put(word, dict.get(word) + 1);
            } else {
                dict.put(word, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : dict.entrySet()) {
            Double wordTf = MathUtil.divideDouble(entry.getValue(), wordCnt, 6);
            tfs.put(entry.getKey(), wordTf);
        }
        return tfs;
    }

    /**
     * 从数据库中获取熟语料库数据 进行 指定词的逆文档频率IDF值计算
     * idf=log(总词数/（该词出现的次数+1）)
     *
     * @param wordAll
     * @return
     */
    private static Map<String, Double> calIDFByDB(List<String> wordAll) {
        LfCorpusService corpusService = Application.getBeanContext().getBean(LfCorpusService.class);
        Long wordSum = Validator.isNotNull(corpusService.getWordSum()) ? corpusService.getWordSum() : 0;
        Map<String, Double> idfs = new HashMap<>();
        for (String word : wordAll) {
            Long wordQuantities = Validator.isNotNull(corpusService.getWordQuantities(word)) ? corpusService.getWordQuantities(word) : 0L;
            Double idf = Math.log(MathUtil.divideDouble(wordSum * 1.0, (wordQuantities + 1), 6));
            idfs.put(word, idf);
        }
        return idfs;
    }

    /**
     * 计算tfidf的值
     *
     * @param tfs
     * @param idfs
     * @return
     */
    private static Map<String, Double> calTFIDF(Map<String, Double> tfs, Map<String, Double> idfs) {
        if (Validator.isNull(tfs) || Validator.isNull(idfs) || tfs.size() != idfs.size()) {
            throw new RuntimeException("tfs或idfs值为空，或单词数相等");
        }
        Map<String, Double> tfidfs = new HashMap<>();
        for (Map.Entry<String, Double> tf : tfs.entrySet()) {
            String currentWord = tf.getKey();
            Double tfValue = tf.getValue();
            Double idfValue = idfs.get(currentWord);
            tfidfs.put(currentWord, new BigDecimal(MathUtil.multiplyDouble(tfValue, idfValue)).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        return tfidfs;
    }

    /**
     * 计算tfidf，供外部调用
     *
     * @param wordAll
     * @return
     */
    public static Map<String, Double> calTFIDF(List<String> wordAll) {
        if (Validator.isNull(wordAll)) {
            throw new RuntimeException("单词总数为空");
        }
        Map<String, Double> tfs = calTF(wordAll);
        Map<String, Double> idfs = calIDFByDB(wordAll);
        return calTFIDF(tfs, idfs);
    }

    /**
     * 计算相对词频；
     * 计算每篇文章相对于并集集合中的词的词频（相对词频）
     *
     * @param wordAll    文章所有的词
     * @param unionWords 并集集合的词
     * @return
     */
    private static Map<String, Double> calOppositeTF(List<String> wordAll, List<String> unionWords) {
        Map<String, Double> tfs = calTF(wordAll);
        Map<String, Double> oppositeTfs = new HashMap<>();
        for (String word : unionWords) {
            oppositeTfs.put(word, tfs.getOrDefault(word, 0.0));
        }
        return oppositeTfs;
    }

    /**
     * 计算cos相似度
     *
     * @param oppositeTFsA
     * @param oppositeTFsB
     * @return
     */
    public static Double calCosSimilarity(Map<String, Double> oppositeTFsA, Map<String, Double> oppositeTFsB) {
        Double[] vectorA = new Double[oppositeTFsA.size()];
        oppositeTFsA.values().toArray(vectorA);
        Double[] vectorB = new Double[oppositeTFsB.size()];
        oppositeTFsB.values().toArray(vectorB);
        return calCosValue(vectorA, vectorB);
    }

    /**
     * 计算cos向量值
     * 公式：cos(θ)=∑(xi+yi)/((√∑(xi*xi) * √∑(yi*yi)))
     * 分子molecular
     * 分母denominator
     *
     * @param vectorA
     * @param vectorB
     * @return
     */
    private static Double calCosValue(Double[] vectorA, Double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            return 2.0000;
        }
        if (Validator.isNull(vectorA) || Validator.isNull(vectorB)) {
            return 2.0000;
        }
        Double molecular = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            System.out.println("a[i]=" + vectorA[i] + "b[i]=" + vectorB[i]);
            molecular += vectorA[i] * vectorB[i];
        }
        Double left = 0.0;
        Double right = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            left += Math.pow(vectorA[i], 2);
            right += Math.pow(vectorB[i], 2);
        }
        if (left * right == 0) {
            return 2.0000;
        }
        Double denominator = Math.sqrt(MathUtil.multiplyDouble(left, right));
        Double result = MathUtil.divideDouble(molecular, denominator, 6);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.parseDouble(df.format(result));
    }

    /**
     * 计算预先相似度，供外部调用的函数
     *
     * @param wordAllA  文本A的所有分词
     * @param wordAllB  文本B的所有分词
     * @param keyWordsA 文本A的所有关键词
     * @param keyWordsB 文本B的所有关键词
     * @return
     */
    public static Double calCosSimilarity(List<String> wordAllA, List<String> wordAllB, List<String> keyWordsA, List<String> keyWordsB) {
        Set<String> unionWords = new HashSet<>();
        unionWords.addAll(keyWordsA);
        unionWords.addAll(keyWordsB);
        Map<String, Double> oppositeTFsA = calOppositeTF(wordAllA, new ArrayList<>(unionWords));
        System.out.println("相对词频a："+oppositeTFsA);
        Map<String, Double> oppositeTFsB = calOppositeTF(wordAllB, new ArrayList<>(unionWords));
        System.out.println("相对词频b："+oppositeTFsB);
        Double cosSimilarity = calCosSimilarity(oppositeTFsA, oppositeTFsB);
        System.out.println(new ArrayList<>(unionWords)+"=>"+cosSimilarity);
        return cosSimilarity;

    }

    public static void main(String[] args) {

        double sqrt = Math.sqrt(0.005202913631633714 / 0.005202913631633714);
        System.out.println(sqrt);
        System.out.println(0.000333554 / 0.0000002);

        Double a = 0.03225806451612903;
        Double b = 0.01111;
        Double c = 0.0;
        c += a * b;
        System.out.println(c + "=" + a * b);
        BigDecimal a1 = new BigDecimal(Double.toString(a));
        BigDecimal b1 = new BigDecimal(Double.toString(b));
        System.out.println(a1.multiply(b1));

        String words = "钱包,车,上掉,司机,说,手机,估计,2019年,3月,24日,哈尔滨,发往,宝清,长途,卧铺,客车,丢失,钱包,身份证,退伍,证,银行卡,退伍证,补办,求,好心人,电话,18345834683,标题,日期,发错";
//        List<String> collect = Arrays.stream(words.split("|")).map(String::valueOf).collect(Collectors.toList());
        List<String> asList = Arrays.asList(words.split(","));
        Map<String, Double> map = calTF(asList);
        System.out.println(map);
    }
}