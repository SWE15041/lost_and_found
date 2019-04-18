package com.lyn.lost_and_found.corpus;

import com.jay.vito.common.util.validate.Validator;
import com.lyn.lost_and_found.segmentation.fnlp.FNLPUtil;
import com.lyn.lost_and_found.utils.FileUtil;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.corpus.StopWords;
import org.fnlp.util.exception.LoadModelException;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//todo 怎么在spring项目中将数据存放到数据库

public class CorpusTraining {


    /**
     * 训练熟语料库，将其保存到数据库
     *
     * @return
     */
    public static Map<String, Long> trainCorpus(String corpusDir) {
        if (!corpusDir.toLowerCase().endsWith("\\")) {
            corpusDir += "\\";
        }
        Map<String, Long> corpusTF = new HashMap<>();
        try {
            //读取生语料库目录
            File[] files = new File(corpusDir).listFiles();
            if (Validator.isNull(files)) {
                throw new RuntimeException("------------------生语料库为空---------------");
            }
            //创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
            CNFactory cnFactory = CNFactory.getInstance();
            StopWords stopWords = new StopWords("E:\\java\\project\\lost_and_found\\src\\main\\resources\\models\\stopwords");
            for (File file : files) {
                //读取文件内容
                List<String> fileContent = FileUtil.getFileContent(file.getAbsolutePath());
                //分词、过滤停用词、获取名词
                List<String> nounWord = FNLPUtil.zhCNSegGetNoun(fileContent.toString());
                for (String word : nounWord) {
                    if (corpusTF.containsKey(word)) {
                        corpusTF.put(word, corpusTF.get(word) + 1);
                    } else {
                        corpusTF.put(word, 1L);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return corpusTF;
    }

    public static Map<String, Long> oldTrainCorpus(String corpusDir) {
        if (!corpusDir.toLowerCase().endsWith("\\")) {
            corpusDir += "\\";
        }
        Map<String, Long> corpusTF = new HashMap<>();
        try {
            //读取生语料库目录
            File[] files = new File(corpusDir).listFiles();
            if (Validator.isNull(files)) {
                throw new RuntimeException("------------------生语料库为空---------------");
            }
            //创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
            CNFactory cnFactory = CNFactory.getInstance("models");
            StopWords stopWords = new StopWords("./models/stopwords");
            for (File file : files) {
                //读取文件内容
                List<String> fileContent = FileUtil.getFileContent(file.getAbsolutePath());
                //分词
                String[] wordAll = cnFactory.seg(fileContent.toString());
                //过滤停用词
                List<String> eliminateStopWords = stopWords.phraseDel(wordAll);
                //计算每个单词出现的次数
                for (String word : eliminateStopWords) {
                    if (corpusTF.containsKey(word)) {
                        corpusTF.put(word, corpusTF.get(word) + 1);
                    } else {
                        corpusTF.put(word, 1L);
                    }
                }
            }
            //遍历分词
            Iterator<Map.Entry<String, Long>> iterator = corpusTF.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> next = iterator.next();
                System.out.println(next.getKey() + ":" + next.getValue());
            }
            System.out.println(corpusTF.size());
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
        return corpusTF;
    }

    public static void main(String[] args) {
        String corpusDir = "E:\\lyn\\毕设\\语料库\\生语料库\\";
        oldTrainCorpus(corpusDir);
    }
}
