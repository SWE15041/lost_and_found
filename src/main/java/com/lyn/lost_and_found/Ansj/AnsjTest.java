package com.lyn.lost_and_found.Ansj;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 使用教程参考： https://blog.csdn.net/bitcarmanlee/article/details/53607776
 */
public class AnsjTest {
    public static void main(String[] args) {
        //1
//        ToAnaliesTest();
//        test();
        //2
//        nlpAnaliesTest();
//        String str ="洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 " +
//                "脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 " +
//                "长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹" ;
//        System.out.println(ToAnalysis.parse(str));
        //3
        StringBuffer fileContent = filterStopWordsTest();
        Result result = NlpAnalysis.parse(fileContent.toString());
        System.out.println("--------中文分词-------");
        System.out.println(result);
        System.out.println("--------去停用词--------");
//        FilterModifWord
    }

    public static void nlpAnaliesTest() {
        Result parse = NlpAnalysis.parse("洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 " +
                "脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 " +
                "长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
        System.out.println(parse);
    }

    public static void ToAnaliesTest() {
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!";
        System.out.println(ToAnalysis.parse(str));
    }

    public static void test() {
        //只关注这些词性的词
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");
            add("v");
            add("vd");
            add("vn");
            add("vf");
            add("vx");
            add("vi");
            add("vl");
            add("vg");
            add("nt");
            add("nz");
            add("nw");
            add("nl");
            add("ng");
            add("userDefine");
            add("wh");
        }};
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!";
        //分词结果的一个封装，主要是一个List<Term>的terms
        Result result = ToAnalysis.parse(str);
        System.out.println(result.getTerms());
        //拿到terms
        List<Term> terms = result.getTerms();
        System.out.println(terms.size());

        for (int i = 0; i < terms.size(); i++) {
            //拿到词
            String word = terms.get(i).getName();
            //拿到词性
            String natureStr = terms.get(i).getNatureStr();
            if (expectedNature.contains(natureStr)) {
                System.out.println(word + ":" + natureStr);
            }
        }
    }

    /**
     * 过滤停用词测试
     */
    public static StringBuffer filterStopWordsTest() {
        // 声明一个可变长的stringBuffer对象
        StringBuffer sb = new StringBuffer("");
        try {
            /*
             * 读取完整文件
             */
            Reader reader = new FileReader("E:\\lyn\\毕设\\test001.txt");
            // 这里我们用到了字符操作的BufferedReader类
            BufferedReader bufferedReader = new BufferedReader(reader);
            // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                sb.append(str + "/n");
                System.out.println(str);
            }
            // 注意这两个关闭的顺序
            bufferedReader.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static void writeIntoFile(String filePath, StringBuffer sb) {

        try {
            /*
             * 完整写入文件
             */
            Writer writer = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(writer);
            // 注意这里调用了toString方法
            bw.write(sb.toString());
            // 注意这两个关闭的顺序
            bw.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
