package com.lyn.lost_and_found.Ansj;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;

/**
 * 使用教程参考： https://blog.csdn.net/bitcarmanlee/article/details/53607776
 */
public class ParticipleDemo {
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

        //4
//        getKeywords();
//
        //5
//        {
//            Map<String, String> args = new HashMap<String, String>();
//            args.put("type", AnsjAnalyzer.TYPE.nlp_ansj.name());
//            args.put(StopLibrary.DEFAULT, "停用词典KEY");
//            args.put(DicLibrary.DEFAULT, "自定义词典KEY");
//            args.put(SynonymsLibrary.DEFAULT, "同义词典KEY");
//            args.put(AmbiguityLibrary.DEFAULT, "歧义词典KEY");
//            args.put("isNameRecognition", "是否开启人名识别, 默认true");
//            args.put("isNumRecognition", "是否开启数字识别, 默认true");
//            args.put("isQuantifierRecognition", "是否开启量词识别, 默认true");
//            args.put("isRealName", "是否保留原字符, 默认false");
//            Analyzer analyzer = new AnsjAnalyzer(args);
//            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
//
//        }
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
     * 取文件内容1
     */
    public static StringBuffer getFileContent(String pathname) {
        // 声明一个可变长的stringBuffer对象
        StringBuffer sb = new StringBuffer("");
        try {
            /*
             * 读取完整文件
             */
//            Reader reader = new FileReader(pathname);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(pathname)), "Utf-8");
            // 这里我们用到了字符操作的BufferedReader类
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                sb.append(str.trim() + "\t");
                System.out.println(str);
            }
            // 注意这两个关闭的顺序
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 取文件内容 2
     */
    public static List<String> getFile(String pathname) {
        // 声明一个可变长的stringBuffer对象
        List<String> sb = new ArrayList<>();
        try {
            /*
             * 读取完整文件
             */
            Reader reader = new FileReader(pathname);
            String encoding = ((FileReader) reader).getEncoding();
            System.out.println("编码："+encoding);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(pathname)), "UTF-8");
            // 这里我们用到了字符操作的BufferedReader类
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                sb.add(str.trim());
//                System.out.println(str);
            }
            // 注意这两个关闭的顺序
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static void buildFile(StringBuffer stringBuffer, String pathname) {
        try {
            Writer writer = new FileWriter(pathname);
            BufferedWriter bw = new BufferedWriter(writer);
            // 注意这里调用了toString方法
            bw.write(stringBuffer.toString());
            // 注意这两个关闭的顺序
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static File partciple(String pathname){
//
//    }

    public static void getKeywords() {
        KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        System.out.println(result);
    }
}
