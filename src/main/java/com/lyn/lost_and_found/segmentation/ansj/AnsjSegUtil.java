package com.lyn.lost_and_found.segmentation.ansj;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;

/**
 * 使用教程参考： https://blog.csdn.net/bitcarmanlee/article/details/53607776
 */
public class AnsjSegUtil {


    public static void nlpAnaliesDemo() {
        Result parse = NlpAnalysis.parse("洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 " +
                "脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 " +
                "长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
        System.out.println(parse);
    }

    public static void ToAnaliesDemo() {
        String string = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!";
        System.out.println(ToAnalysis.parse(string));
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

    public static void getKeywordsDemo() {
        KeyWordComputer kwc = new KeyWordComputer(5);
//        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
//        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        String title = "寻物启事";
        String content = "2019，3.7在杭州惠兴中学附近丢失一男款黑色旗帜钱包，，里面有身份证，建设银行卡和农村信用社的银行卡，还有一张会员卡和现金若干。因为是河北的证件，补办会很麻烦，所以希望捡到者可以归还，我这个月的生活费都在里面\n";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        System.out.println(result);
    }
    /**
     * 给新词标注词性
     */
    static void natureRecognitionDemo() {
        String[] strs = {"对", "非", "ansj", "的", "分词", "结果", "进行", "词性", "标注"};
        List<String> lists = Arrays.asList(strs);
        NatureRecognition natureRecognition = new NatureRecognition();
        List<Term> recognition = natureRecognition.recognition(lists, 0);
        System.out.println(recognition);
    }

    public static void main(String[] args) {

        Result parse = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
        Iterator<Term> iterator = parse.getTerms().iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getName());
        }
        NatureRecognition natureRecognition = new NatureRecognition();
        List<Term> recognition = natureRecognition.recognition(list, 0);
        System.out.println(recognition);

    }
}
