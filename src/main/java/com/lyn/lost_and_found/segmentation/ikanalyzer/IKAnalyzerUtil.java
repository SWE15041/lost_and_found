package com.lyn.lost_and_found.segmentation.ikanalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKAnalyzerUtil {

    /**
     * 利用tokenStream进行分词，分词效果不准确，比如：地方名
     *
     * @param stringReader
     */
    public static void ikTokenStreamAnalyzer(StringReader stringReader) {
//        new StringReader("这是一个中文分词的例子，你可以直接运行它！IKAnalyer can analysis english text too")
        //构建IK分词器，使用smart分词模式
        Analyzer analyzer = new IKAnalyzer(true);

        //获取Lucene的TokenStream对象
        TokenStream ts = null;
        try {
            ts = analyzer.tokenStream("myfield", stringReader);
            //获取词元位置属性
            OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
            //获取词元文本属性
            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
            //获取词元类型属性
            TypeAttribute type = ts.addAttribute(TypeAttribute.class);
            //重置TokenStream（重置StringReader）
            ts.reset();
            //迭代获取分词结果
            while (ts.incrementToken()) {
//                System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
                System.out.print(term.toString() + " | ");
            }

            //关闭TokenStream（关闭StringReader）
            ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放TokenStream的所有资源
            if (ts != null) {
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void ikDefineAnalyzer(StringReader stringReader) {
        //useSmart=true表示采用智能分词
        IKAnalyzer ikAnalyzer = new IKAnalyzer(true);
        //todo 可载人自定义词典
        Configuration configuration = DefaultConfig.getInstance();
        Dictionary dictionary = Dictionary.initial(configuration);
        System.out.println(configuration.getExtDictionarys());
        System.out.println(configuration.getExtStopWordDictionarys());
        System.out.println(configuration.getMainDictionary());
        System.out.println(configuration.getQuantifierDicionary());
        List<String> mydict = new ArrayList<>();
        mydict.add("这个月");
        mydict.add("捡到者");
        mydict.add("惠兴中学");
        Dictionary.getSingleton().addWords(mydict);
        TokenStream tokenStream = null;
        try {
            tokenStream = ikAnalyzer.tokenStream("", stringReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                System.out.print(term.toString() + " | ");
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void ikSegAnalyzer(StringReader stringReader) {
        IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
        Lexeme lexeme = null;
        try {
            while ((lexeme = ikSegmenter.next()) != null) {
                System.out.print(lexeme.getLexemeText() + " | ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String str = "本人当日中午携带手包沿着新市医院北门行走，不慎遗失。内有身份证（井女士）和银行卡（交通银行金邻卡），本人非常着急，请拾到者归还证件，必有重谢！\n";
        //        String str = "今天丢了一串钥匙";
//        String str="我喜欢看电视，不喜欢看电影。";
//  String str="公路局正在治理解放大道路面积水问题";
//        String str = "2019.3.7在杭州惠兴中学附近丢失一男款黑色旗帜钱包，里面有身份证，建设银行卡和农村信用社的银行卡，还有一张会员卡和现金若干。因为是河北的证件，补办会很麻烦，所以希望捡到者可以归还，我这个月的生活费都在里面\n";
//        ikTokenStreamAnalyzer(new StringReader(str));
//        System.out.println();
//        ikSegAnalyzer(new StringReader(str));
//        System.out.println();
//        ikDefineAnalyzer(new StringReader(str));

//        Result result = NlpAnalysis.parse(str).recognition(StopLibrary.get());
//        System.out.println(result);


    }

}
