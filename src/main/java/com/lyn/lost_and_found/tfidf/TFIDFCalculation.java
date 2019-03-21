package com.lyn.lost_and_found.tfidf;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.website.core.Application;
import com.lyn.lost_and_found.service.LfCorpusService;

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
            Double wordTf = (entry.getValue() * 1.0) / wordCnt;
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
        Long wordSum = corpusService.getWordSum();
        Map<String, Double> idfs = new HashMap<>();
        for (String word : wordAll) {
            Long wordQuantities = corpusService.getWordQuantities(word);
            Double idf = Math.log(wordSum * 1.0 / (wordQuantities + 1));
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
            tfidfs.put(currentWord, tfValue * idfValue);
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
    static Map<String, Double> calOppositeTF(List<String> wordAll, List<String> unionWords) {
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
        Double result = molecular / Math.sqrt(left * right);
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
        Map<String, Double> oppositeTFsB = calOppositeTF(wordAllB, new ArrayList<>(unionWords));
        return calCosSimilarity(oppositeTFsA, oppositeTFsB);
    }

}