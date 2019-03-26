package com.lyn.lost_and_found.segmentation.fnlp;

import com.jay.vito.common.util.validate.Validator;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.corpus.StopWords;
import org.fnlp.util.exception.LoadModelException;

import java.util.ArrayList;
import java.util.List;

//todo 1将中文处理工厂配置成项目启动时完成初始化 2停用词的过滤

public class FNLPUtil {

    /**
     * 中文分词：
     * 1.分词；
     * 2.停用词过滤；
     * 3.获取名词；
     *
     * @param text 要过滤的文本
     * @return
     */
    public static List<String> zhCNSegGetNoun(String text) {
        if (Validator.isNull(text)) {
            return null;
        }
        // 创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
        CNFactory factory = null;
        try {
            factory = CNFactory.getInstance("models");
        } catch (LoadModelException e) {
            throw new RuntimeException("分词模型载入异常");
        }
        //加载、配置停用词词典
        StopWords stopWords = new StopWords("./models/stopwords");

        // 使用分词器对中文句子进行分词，得到分词结果{{单词...}，{词性...}}
        String[][] tags = factory.tag(text);

        //过滤停用词、再保留名词
        List<String> nounWords = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            for (int j = 0; j < tags[i].length; j++) {
                if (i + 1 <= tags.length - 1) {
                    String word = tags[i][j];
                    String pos = tags[i + 1][j];
                    boolean isStopWord = stopWords.isStopWord(word);
                    if (!isStopWord) {
                        if (pos.equals("名词")||pos.equals("形容词")||pos.equals("地名")||pos.equals("人名")) {
                            nounWords.add(word);
                        }
                    }
                    System.out.println(word+":"+pos);
                }
            }
        }
        return nounWords;
    }

    public static void main(String[] args) throws Exception {

        // 创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
        CNFactory factory = CNFactory.getInstance("models");

        // 使用分词器对中文句子进行分词，得到分词结果
//        String str = "我不喜欢看电视，也不喜欢看电影。";
//        String str = "2019.3.7在杭州惠兴中学附近丢失一男款黑色旗帜钱包,里面有身份证，建设银行卡和农村信用社的银行卡，还有一张会员卡和现金若干。因为是河北的证件，补办会很麻烦，所以希望捡到者可以归还，我这个月的生活费都在里面";
//        String str="关注自然语言处理、语音识别、深度学习等方向的前沿技术和业界动态。";
        String str = "黑色长条钱包一个，内有大量银行卡，现金可以不要,只求好心人归还钱包。丢失时间是3月24号凌晨一点左右。希望好心人归还，必有重谢。";
        String[] words = factory.seg(str);
        //过滤停用词
        StopWords stopWords = new StopWords("./models/stopwords");
        List<String> filterWords = stopWords.phraseDel(words);
        // 打印分词结果
        for (String word : filterWords) {
            System.out.print(word + " ");
        }
        System.out.println();
        //分词
        //todo error:分词返回的词性中 逗号的词性为名词
        String[][] tags = factory.tag(str);
        List<String> nounWords = new ArrayList<>();
//        for (int i = 0; i < tags.length; i++) {
//            for (int j = 0; j < tags[i].length; j++) {
//                if (i + 1 <= tags.length - 1) {
//                    String word = tags[i][j];
//                    boolean isStopWord = stopWords.isStopWord(word);
//                    if (!isStopWord) {
//                        String pos = tags[i + 1][j];
//                        if (pos.equals("名词")) {
//                            nounWords.add(word);
//                        }
//                    }
//                }
//            }
//        }
        System.out.println(nounWords);


//        List<String> strings = zhCNSegGetNoun(str);
    }
}
//    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> next = iterator.next();
//            String key = next.getKey();
//            String value = next.getValue();
//            System.out.println(key + "：" + value);
//        }