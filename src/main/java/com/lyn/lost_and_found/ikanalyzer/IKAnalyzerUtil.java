package com.lyn.lost_and_found.ikanalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

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
                System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
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

    public static void ikSegmentAnalyzer() {

    }

    public static void main(String[] args) {
        String str = "2019.3.7在杭州惠兴中学附近丢失一男款黑色旗帜钱包，里面有身份证，建设银行卡和农村信用社的银行卡，还有一张会员卡和现金若干。因为是河北的证件，补办会很麻烦，所以希望捡到者可以归还，我这个月的生活费都在里面\n";
        ikTokenStreamAnalyzer(new StringReader(str));
    }

}
