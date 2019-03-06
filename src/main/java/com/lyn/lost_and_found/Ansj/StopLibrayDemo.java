package com.lyn.lost_and_found.Ansj;

import org.ansj.domain.Result;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class StopLibrayDemo {


    public static void main(String[] args) {
        MyDefineStopLibraryTest();
    }

    /**
     * 在项目跟目录下创建library/stop.dic文件，文件内容的格式为（其中停用词类型可为空，之间的空格用制表符\t：词语 停用词类型
     */
    public static void MyDefineStopLibraryTest() {
        System.out.println("----------不加停用词过滤时-----------");
        Result parse = NlpAnalysis.parse("洁面仪器配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 " +
                "脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 " +
                "长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
        System.out.println(parse);
        Result parses = NlpAnalysis.parse("洁面仪器配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 " +
                "脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 " +
                "长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹").recognition(StopLibrary.get());
        System.out.println("-------加入停用词过滤后--------");
        System.out.println(parses);
    }

    /**
     * 另一种对自定义停用词的使用
     */
    public void stopTest() {
        StopLibrary.insertStopWords(StopLibrary.DEFAULT, "的", "呵呵", "哈哈", "噢", "啊");
        Result terms = ToAnalysis.parse("英文版是小田亲自翻译的");
        //使用停用词
        System.out.println(terms.recognition(StopLibrary.get()));
    }
}
