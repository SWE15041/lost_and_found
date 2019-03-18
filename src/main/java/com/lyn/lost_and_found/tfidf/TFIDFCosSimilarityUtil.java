package com.lyn.lost_and_found.tfidf;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.text.DecimalFormat;
import java.util.*;

public class TFIDFCosSimilarityUtil {

    /**
     * 第一步，分词。
     * 第二步，计算每个文本的关键词。
     * 第三步，列出所有的关键词。
     * 第四步，计算词频。
     * 第五步，写出词频向量。
     * 第六步，计算余弦相似度
     *
     * @param passageA
     * @param passageB
     */
    public static Double calCosineSimilarity(String passageA, String passageB) {
//        Result parseA = NlpAnalysis.parse(passageA).recognition(StopLibrary.get());
//        Result parseB = NlpAnalysis.parse(passageB).recognition(StopLibrary.get());
        Result parseA = NlpAnalysis.parse(passageA);
        Result parseB = NlpAnalysis.parse(passageB);
        //列出所有的单词 到集合unionWords
        Set<String> unionWords = new HashSet<>();
        for (Term term : parseA) {
            unionWords.add(term.getName());
        }
        for (Term term : parseB) {
            unionWords.add(term.getName());
        }
        System.out.println("common:\t" + unionWords);
        Map<String, Double> dictA = calTF(unionWords, parseA.getTerms());
        Map<String, Double> dictB = calTF(unionWords, parseB.getTerms());
        System.out.println("DA\t" + dictA.size() + "\t" + dictA);
        System.out.println("DB\t" + dictB.size() + "\t" + dictB);
        int unionWordnum = unionWords.size();
        System.out.println("unionWords:\t" + unionWordnum);
        Double[] aVector = getMapValues(dictA);
        Double[] bVector = getMapValues(dictB);
        unionWords.clear();
        return calCosVectorValue(aVector, bVector);
    }

    /**
     * 计算余弦相似度
     *
     * @param textA
     * @param textB
     * @return
     */
    public static Double calCosSimilarity(String textA, String textB) {
        //分词
        Result parseA = NlpAnalysis.parse(textA);
        Result parseB = NlpAnalysis.parse(textB);
        //计算词频<单词，词频>
        Map<String, Double> textATFs = TFIDFUtil.calTFs(parseA.getTerms());
        Map<String, Double> textBTFs = TFIDFUtil.calTFs(parseB.getTerms());
        //计算逆文档频率
        String corpusDirPath = "e:\\corpus";
        List<String> wordAList = getMapKeys(textATFs);
        List<String> wordBList = getMapKeys(textBTFs);
        Map<String, Double> textAIDFs = TFIDFUtil.calIDFs(wordAList, corpusDirPath);
        Map<String, Double> textBIDFs = TFIDFUtil.calIDFs(wordBList, corpusDirPath);
        //计算tfidf
        Map<String, Double> textAIFTDFs = TFIDFUtil.calTFIDFs(textATFs, textAIDFs);
        Map<String, Double> textBTFIDFs = TFIDFUtil.calTFIDFs(textBTFs, textBIDFs);
        //取关键词 keyWordNum：关键词数量
        Integer keyWordNum = 5;
        List<String> textAkeyWords = TFIDFUtil.getKeyWords(textAIFTDFs, keyWordNum);
        List<String> textBkeyWords = TFIDFUtil.getKeyWords(textBTFIDFs, keyWordNum);
        //取关键词词频向量
        Map<String, Double> textAkeyWordTFs = new HashMap<>();
        for (String textAkeyWord : textAkeyWords) {
            textAkeyWordTFs.put(textAkeyWord, textATFs.get(textAkeyWord));
        }
        Map<String, Double> textBkeyWordTFs = new HashMap<>();
        for (String textBkeyWord : textBkeyWords) {
            textBkeyWordTFs.put(textBkeyWord, textBTFs.get(textBkeyWord));
        }
        //计算余弦相似度
        Double[] textATFValue = getMapValues(textAkeyWordTFs);
        Double[] textBTFValue = getMapValues(textBkeyWordTFs);
        Double cosSimilarity = calCosVectorValue(textATFValue, textBTFValue);
        return cosSimilarity;

    }

    /**
     * 取map的所有value
     *
     * @param map
     * @return
     */
    static Double[] getMapValues(Map<String, Double> map) {
        Iterator<Map.Entry<String, Double>> iteratorA = map.entrySet().iterator();
        int arrarySize = map.size();
        Double[] values = new Double[arrarySize];
        int i = 0;
        while (iteratorA.hasNext()) {
            values[i] = iteratorA.next().getValue();
            i++;
        }
        return values;
    }

    /**
     * 取map的所有key
     *
     * @return
     */
    static List<String> getMapKeys(Map<String, Double> map) {
        Iterator<Map.Entry<String, Double>> iterator = map.entrySet().iterator();
        List<String> keys = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            keys.add(next.getKey());
        }
        return keys;
    }

    /**
     * 计算指定文本中，指定词频的数量
     *
     * @param unionWords
     * @return
     */
    public static Map<String, Double> calTF(Set<String> unionWords, List<Term> wordALL) {
        //存放（单词，单词词频）
        HashMap<String, Double> tf = new HashMap<>();
        Map<String, Double> dict = new HashMap<>();
        for (String unionWord : unionWords) {
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
        return tf;
    }

    /**
     * 计算余弦相似度的值
     * 公式：cos(θ)=∑(xi+yi)/((√∑(xi*xi) * √∑(yi*yi)))
     * 分子molecular
     *
     * @param A
     * @param B
     * @return
     */
    private static Double calCosVectorValue(Double[] A, Double[] B) {
        if (A.length != B.length) {
            return 2.0000;
        }
        if (A == null || B == null) {
            return 2.0000;
        }
        Double molecular = 0.0;
        for (int i = 0; i < A.length; i++) {
            System.out.println("a[i]=" + A[i] + "b[i]=" + B[i]);
            molecular += A[i] * B[i];
        }
        Double left = 0.0;
        Double right = 0.0;
        for (int i = 0; i < A.length; i++) {
            left += Math.pow(A[i], 2);
            right += Math.pow(B[i], 2);
        }
        if (left * right == 0) {
            return 2.0000;
        }
        Double result = molecular / Math.sqrt(left * right);
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.parseDouble(df.format(result));
    }

    public static void main(String[] args) {
        String passageA = "丁瑜 在2019.25下午赶广州地铁时遗失身份证一张，如拾到都请联系本人138297837886，本人急须身份证参加艺考，谢谢！";
        String passageB = "本人于2019年2月27号晚上9点左右在长水机场不慎丢失身份证， 姓名 李奎 汉族 ，户籍 河南 ，签发机关 项城市公安局，身份证号 4127021992042... 请捡到的朋友联系我 必酬谢 ！\n";
//        String passageA = "我喜欢看电视，不喜欢看电影。";
//        String passageB = "我不喜欢看电视，也不喜欢看电影。";
        Double cosValue = calCosineSimilarity(passageA, passageB);
        System.out.println("余弦值：\t" + cosValue);
//
        int x = 55;
        int y = 34;
        System.out.println(Math.sqrt(Math.pow(x, 2) * Math.pow(y, 2)));
        System.out.println(Math.sqrt(Math.pow(x, 2)) * Math.sqrt(Math.pow(y, 2)));
    }
}
