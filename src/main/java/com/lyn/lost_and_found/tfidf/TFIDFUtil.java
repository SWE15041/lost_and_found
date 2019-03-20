package com.lyn.lost_and_found.tfidf;

import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.service.LfCorpusService;
import com.lyn.lost_and_found.utils.FileUtil;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TFIDFUtil {

    //    private static Double TF;
        private static Double IDF;
//    private static Double TFIDF;
    private static Long fileNum = 0L;
    @Value("${corpus.path}")
    private static String corpusDir;
    @Autowired
    private LfCorpusService corpusService;

    /**
     * 计算文本中每个分词的词频TF;
     * TF（词频） = 某个词在文章中出现的次数；
     * = 某个词在文章中出现的次数 / 该文章的总词数；(当前使用这个)
     * = 某个词在文章中出现的次数 / 该文出现次数最多的词的出现次数；
     *
     * @param wordAll
     * @return
     */
    public static Map<String, Double> calTFs(List<Term> wordAll) {
        //存放<单词，单词数量>
        Map<String, Integer> dict = new HashMap<>();
        //存放（单词，单词词频）
        Map<String, Double> tf = new HashMap<>();
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
    private static Double calIDF(String word, String corpusDirPath) {
        File[] corpus = new File(corpusDirPath).listFiles();
        if (Validator.isNull(corpus)) {
            return null;
        }
        //语料库文档总数；
        Long cntFileNum = FileUtil.cntFileNum(new File(corpusDirPath));
        //包含指定词的文档数；
        Long includeWordFileNum = 0L;
        for (File file : corpus) {
            if (file.isFile()) {
                //文件的分词读取；
                List<String> words = FileUtil.getFileContent(file.getAbsolutePath());
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

    /**
     * 从数据库中获取熟语料库数据进行IDF值计算
     * idf=log(语料库总词数/（该词出现的次数+1）)
     *
     * @param word
     * @return
     */
    private Double calIDFByDB(String word) {
        Long wordSum = corpusService.getWordSum();
        Long wordQuantities = corpusService.getWordQuantities(word);
        IDF = Math.log(wordSum * 1.0 / (wordQuantities + 1));
        return IDF;
    }

    /**
     * 计算每个单词的逆文档频率
     * IDF=log(语料库文档总数/包含该词的文档数+1)；
     *
     * @param words
     * @param corpusDirPath
     * @return
     */
    public static Map<String, Double> calIDFs(List<String> words, String corpusDirPath) {
        Map<String, Double> idfs = new HashMap<>();
        for (String word : words) {
            Double idf = calIDF(word, corpusDirPath);
//            Double idf = calIDFByDB(word);
            if (Validator.isNotNull(idf)) {
                idfs.put(word, idf);
            }
        }
        return idfs;
    }

    /**
     * 计算每个单词的tf-idf值
     *
     * @param TFs
     * @param IDFs
     * @return
     */
    public static Map<String, Double> calTFIDFs(Map<String, Double> TFs, Map<String, Double> IDFs) {
        int tfSize = TFs.size();
        int idfSize = IDFs.size();
        if (tfSize != idfSize) {
            return null;
        }
        Map<String, Double> tfidf = new HashMap<>();
        for (Map.Entry<String, Double> tf : TFs.entrySet()) {
            String word = tf.getKey();
            Double tfValue = tf.getValue();
            Double idfValue = IDFs.get(word);
            tfidf.put(word, tfValue * idfValue);
        }
        return tfidf;
    }

    /**
     * 取出指定关键词
     *
     * @param iftdf
     * @param keyWordNum
     * @return
     */
    public static List<String> getKeyWords(Map<String, Double> iftdf, Integer keyWordNum) {
        //倒序
        List<Map.Entry<String, Double>> entryList = iftdf.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).collect(Collectors.toList());
        Iterator<Map.Entry<String, Double>> iterator = entryList.iterator();
        List<String> words = new ArrayList<>();
        int size = iftdf.size();
        if (size < keyWordNum) {
            keyWordNum = size;
        }
        while (iterator.hasNext()) {
            if (keyWordNum < 0) {
                break;
            }
            keyWordNum--;
            Map.Entry<String, Double> next = iterator.next();
            String word = next.getKey();
            words.add(word);
        }
        return words;
    }

//    /**
//     * 计算指定目录下文件的数量
//     *
//     * @param dir
//     * @return
//     */
//    public static Long cntFileNum(File dir) {
//        File[] files = dir.listFiles();
//        for (File file : files) {
//            if (file.isDirectory()) {
//                cntFileNum(file);
//            } else {
//                fileNum++;
//            }
//        }
//        return fileNum;
//    }
//

    public static void main(String[] args) {
        String path = "E:\\corpus";
        File file = new File(path);

//        Long cntFileNum = cntFileNum(file);
//        System.out.println("文件数量：" + cntFileNum);
//        Double idfV = calIDFs("工作证", path);
//        System.out.println("idf =\t" + idfV);

        Result parse = NlpAnalysis.parse("本人于3月6日在官渡区塘子巷地铁站附近丢失一本铁路工作证，内有身份证一张和昆明市公交卡。工作证，身份证名字均为陶俊宇。工作证是本红色外壳的。");

        Map<String, Double> tfs = calTFs(parse.getTerms());


        Iterator<Map.Entry<String, Double>> iterator = tfs.entrySet().iterator();
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

