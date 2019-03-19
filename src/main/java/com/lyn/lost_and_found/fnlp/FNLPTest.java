package com.lyn.lost_and_found.fnlp;

import org.fnlp.nlp.cn.CNFactory;

public class FNLPTest {
    public static void main(String[] args) throws Exception {

        // 创建中文处理工厂对象，并使用“models”目录下的模型文件初始化
        CNFactory factory = CNFactory.getInstance("models");

        // 使用分词器对中文句子进行分词，得到分词结果
//        String str="我不喜欢看电视，也不喜欢看电影。";
        String str="\"2019.3.7在杭州惠兴中学附近丢失一男款黑色旗帜钱包，，里面有身份证，建设银行卡和农村信用社的银行卡，还有一张会员卡和现金若干。因为是河北的证件，补办会很麻烦，所以希望捡到者可以归还，我这个月的生活费都在里面\\n\"";
//        String str="关注自然语言处理、语音识别、深度学习等方向的前沿技术和业界动态。";
        String[] words = factory.seg(str);

        // 打印分词结果
        for(String word : words) {
            System.out.print(word + " ");
        }
        System.out.println();
    }
}
