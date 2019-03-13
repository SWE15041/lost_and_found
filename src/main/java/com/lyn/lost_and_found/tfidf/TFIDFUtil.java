package com.lyn.lost_and_found.tfidf;

import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.Ansj.ParticipleDemo;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TFIDFUtil {

    //    private static Double TF;
//    private static Double IDF;
//    private static Double TFIDF;
    private static Long fileNum = 0L;
    @Value("${corpus.path}")
    private static String corpusDir;

    /**
     * 计算文本中每个分词的词频TF;
     * TF（词频） = 某个词在文章中出现的次数；
     * = 某个词在文章中出现的次数 / 该文章的总词数；(当前使用这个)
     * = 某个词在文章中出现的次数 / 该文出现次数最多的词的出现次数；
     *
     * @param wordAll
     * @return
     */
    public static HashMap<String, Double> calTFs(List<Term> wordAll) {
        //存放<单词，单词数量>
        Map<String, Integer> dict = new HashMap<>();
        //存放（单词，单词词频）
        HashMap<String, Double> tf = new HashMap<>();
        //单词数
        Integer wordCnt = 0;
        for (Term term : wordAll) {
            wordCnt++;
            String word = term.getName();
            if (dict.containsKey(word)) {
                dict.put(word, dict.get(word) + 1);
            } else {
                dict.put(word, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : dict.entrySet()) {
            Double wordTf = (entry.getValue() * 1.0) / wordCnt;
            tf.put(entry.getKey(), wordTf);
        }
        return tf;
    }

    /**
     * 计算当前分词的逆文档频率IDF；
     * 公式:
     * IDF=log(语料库文档总数/包含该词的文档数+1)；
     * 如果一个词越常见，那么分母就越大，逆文档频率就越小越接近0。分母之所以要加1，是为了避免分母为0（即所有文档都不包含该词）。log表示对得到的值取对数。
     *
     * @param word
     * @param corpusDirPath
     * @return
     */
    public static Double calIDF(String word, String corpusDirPath) {
        File corpus = new File(corpusDirPath);
        Long cntFileNum = cntFileNum(corpus);
        File[] dir = new File(corpusDirPath).listFiles();
        if (Validator.isNull(dir)) {
            return null;
        }
        Long includeWordFileNum = 0L;
        for (File file : dir) {
            if (file.isFile()) {
                //文件的分词读取；
                List<String> words = ParticipleDemo.getFileCont(file.getAbsolutePath());
//                System.out.println(words.toString());
                Result parse = NlpAnalysis.parse(words.toString()).recognition(StopLibrary.get());
//                String s = NlpAnalysis.parse(words.toString()).recognition(StopLibrary.get()).toStringWithOutNature();
                ////                System.out.println(parse.toString());
                ////                System.out.println(s);
                //                //计算语料库中包含指定词语word的文件数量
                for (Term term : parse) {
                    if (term.getName().contains(word)) {
                        includeWordFileNum++;
                    }
                }
            }
        }
        Double IDF = Math.log((cntFileNum * 1.0) / (includeWordFileNum + 1));
        return IDF;
    }

//    public static Double calTFIDF(){
//
//    }

    /**
     * 计算指定目录下文件的数量
     *
     * @param dir
     * @return
     */
    public static Long cntFileNum(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                cntFileNum(file);
            } else {
                fileNum++;
            }
        }
        return fileNum;
    }


    public static void main(String[] args) {
        String path = "E:\\corpus";
        File file = new File(path);

//        Long cntFileNum = cntFileNum(file);
//        System.out.println("文件数量：" + cntFileNum);
//        Double idfV = calIDF("工作证", path);
//        System.out.println("idf =\t" + idfV);

        Result parse = NlpAnalysis.parse("本人于3月6日在官渡区塘子巷地铁站附近丢失一本铁路工作证，内有身份证一张和昆明市公交卡。工作证，身份证名字均为陶俊宇。工作证是本红色外壳的。");

        HashMap<String, Double> tFs = calTFs(parse.getTerms());


        Iterator<Map.Entry<String, Double>> iterator = tFs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            String word = next.getKey();
            Double tf = next.getValue();
            Double idf = calIDF(word, path);
            double tfIdf = tf * idf;
            System.out.println(word + ":\ttf=" + tf + "\tidf=" + idf + "\ttfIDF=" + tfIdf);
        }
    }
}

