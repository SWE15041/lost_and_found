package com.lyn.lost_and_found.tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFDemo {

    private static Double TF;
    private static Double IDF;
    private static Double TFIDF;

    /**
     * 计算段落的TF IDF TFIDF
     * TF词频=某个词在文章中出现的次数；
     * =某个词在文章中出现的次数/该文章的总词数；(当前使用这个)
     * =某个词在文章中出现的次数 /该文出现次数最多的词的出现次数；
     *
     * @param wordAll
     * @return
     */
    public static   HashMap<String, Double>  calTFIDF(List<String> wordAll) {
        //存放<单词，单词数量>
        Map<String, Integer> dict = new HashMap<>();
        //存放（单词，单词词频）
        HashMap<String, Double> tf = new HashMap<>();
        //单词数
        Integer wordCnt = 0;
        for (String word : wordAll) {
            wordCnt++;
            if (dict.containsKey(word)) {
                dict.put(word, dict.get(word) + 1);
            }
            dict.put(word, 1);
        }
        for(Map.Entry<String, Integer> entry:dict.entrySet()){
            Double wordTf=(entry.getValue()*1.0) / wordCnt;
            tf.put(entry.getKey(), wordTf);
        }
        return tf;
    }



}
