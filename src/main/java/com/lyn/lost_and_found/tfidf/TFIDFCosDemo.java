package com.lyn.lost_and_found.tfidf;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.text.DecimalFormat;
import java.util.*;

public class TFIDFCosDemo {

    /**
     * <单词，词频>
     */
    public static Map<String, Double> dictA = new HashMap<>();
    public static Map<String, Double> dictB = new HashMap<>();

    /**
     * 第一步，分词。
     * 第二步，列出所有的词。
     * 第三步，计算词频。
     * 第四步，写出词频向量。
     *
     * @param passageA
     * @param passageB
     */
    public static Double calTFVector(String passageA, String passageB) {
        Result parseA = NlpAnalysis.parse(passageA).recognition(StopLibrary.get());
        Result parseB = NlpAnalysis.parse(passageB).recognition(StopLibrary.get());
//        Result parseA = NlpAnalysis.parse(passageA);
//        Result parseB = NlpAnalysis.parse(passageB);
        Set<String> unionWords = new HashSet<>();
        for (Term term : parseA) {
            unionWords.add(term.getName());
        }
        for (Term term : parseB) {
            unionWords.add(term.getName());
        }
        System.out.println("common:\t" + unionWords);
        dictA = calTF(unionWords, parseA.getTerms(), dictA);
        dictB = calTF(unionWords, parseB.getTerms(), dictB);
        int unionWordnum = unionWords.size();
        System.out.println("unionWords:\t" + unionWordnum);
        System.out.println("DA\t" + dictA.size() + "\t" + dictA);
        System.out.println("DB\t" + dictB.size() + "\t" + dictB);
        Double[] A = getKeyValue(dictA, unionWordnum);
        Double[] B = getKeyValue(dictB, unionWordnum);
        return cosineSimilarity(A, B);

    }

    static Double[] getKeyValue(Map<String, Double> map, Integer arrarySize) {
        Iterator<Map.Entry<String, Double>> iteratorA = map.entrySet().iterator();
        Double[] A = new Double[arrarySize];
        int i = 0;
        while (iteratorA.hasNext()) {
            A[i] = iteratorA.next().getValue();
            i++;
        }
        return A;
    }

    /**
     * 计算指定文本中，指定词频的数量
     *
     * @param UnionWords
     * @param dict
     * @return
     */
    public static Map<String, Double> calTF(Set<String> UnionWords, List<Term> wordALL, Map<String, Double> dict) {
        //存放（单词，单词词频）
        HashMap<String, Double> tf = new HashMap<>();
        for (String unionWord : UnionWords) {
            int cnt = 0;
            for (Term term : wordALL) {
                String word = term.getName();
                if (unionWord.equals(word)) {
                    cnt++;
                }
            }
            dict.put(unionWord, cnt * 1.0);
        }
        for (Map.Entry<String, Double> entry : dict.entrySet()) {
            Double wordTf = (entry.getValue() * 1.0) / wordALL.size();
            tf.put(entry.getKey(), wordTf);
        }
        return dict;
    }

    public static Double cosineSimilarity(Double[] A, Double[] B) {
        if (A.length != B.length) {
            return 2.0000;
        }
        if (A == null || B == null) {
            return 2.0000;
        }
        long fenzi = 0;
        for (int i = 0; i < A.length; i++) {
            fenzi += A[i] * B[i];
        }
        long left = 0;
        long right = 0;
        for (int i = 0; i < A.length; i++) {
            left += A[i] * A[i];
            right += B[i] * B[i];
        }
        if (left * right == 0) {
            return 2.0000;
        }
        double result = fenzi / Math.sqrt(left * right);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.parseDouble(df.format(result));
    }

    public static void main(String[] args) {
        String passageA = "丁瑜 在2019.25下午赶广州地铁时遗失身份证一张，如拾到都请联系本人138297837886，本人急须身份证参加艺考，谢谢！";
        String passageB = "本人于2019年2月27号晚上9点左右在长水机场不慎丢失身份证， 姓名 李奎 汉族 ，户籍 河南 ，签发机关 项城市公安局，身份证号 4127021992042... 请捡到的朋友联系我 必酬谢 ！\n";
//        String passageA = "我喜欢看电视，不喜欢看电影。";
//        String passageB = "我不喜欢看电视，也不喜欢看电影。";
        Double cosValue = calTFVector(passageA, passageB);
        System.out.println("余弦值：\t" + cosValue);
    }
}
