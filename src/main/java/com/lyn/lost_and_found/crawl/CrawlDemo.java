package com.lyn.lost_and_found.crawl;

import com.lyn.lost_and_found.utils.FileUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrawlDemo {
    public static void main(String[] args) {
        List<String> pageUrls = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String pageUrl = "http://www.cswzl.com/lostinfo.action?q.currentPageNum=" + String.valueOf(i)
                    + "&q.t=1&q.altercond=false";
            pageUrls.add(pageUrl);
        }
        try {
            for (String pageUrl : pageUrls) {
                System.out.println("-----------失物招领的页面: " + pageUrl + " -----------------------");
                List<String> hypeLink = getHypeLink(pageUrl);
                for (String url : hypeLink) {
                    System.out.println("--------------爬取数据的url:" + url + " ---------------------");
                    String webInfo = getWebInfo(url);
                    String filename = UUID.randomUUID().toString().replace("-", "") + ".txt";
                    String path = "E:\\lyn\\毕设\\语料库\\生语料库\\" + filename;
                    FileUtil.writeFile(path, webInfo);
                    Thread.sleep(3000);
//                    File file = new File(path);
//                    File parentFile = file.getParentFile();
//                    if (!parentFile.exists()) {
//                        parentFile.mkdirs();
//                    }
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
//                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//                    //加入换行符
//                    bufferedWriter.write(webInfo + "\n");
//
//                    bufferedWriter.close();
//                    outputStreamWriter.close();
//                    fileOutputStream.close();
                }
                hypeLink.clear();
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取失物招领的 描述信息
     *
     * @param url
     */
    public static String getWebInfo(String url) {
        Connection connection = Jsoup.connect(url).timeout(10000);
        String descripInfo = null;
        try {
            Document document = connection.get();
            Element element = document.select("div[class=div_body]").select("tr").get(4).select("tr").get(3);
            descripInfo = element.select("td").get(1).text();
            System.out.println("描述信息：" + descripInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descripInfo;
    }

    /**
     * 获取 a标签的超链接地址
     *
     * @param url 失物招领的指定分页地址
     * @return
     */
    public static List<String> getHypeLink(String url) {
        List<String> hrefUrl = new ArrayList<>();
        try {
            String prefix = "http://www.cswzl.com";
            Connection connection = Jsoup.connect(url).timeout(10000);
            Document document = connection.get();
            System.out.println(document.body());
            Elements aTags = document.getElementsByTag("a");
//            System.out.println(aTags);
            Element element = document.select("form#lostinfo").get(0).select(".info_title1").get(0);
//            System.out.println(element);i
            Elements aElement = element.select("a");

            System.out.println(aElement);
            for (Element a : aElement) {
                String href = prefix + a.attr("href");
                hrefUrl.add(href);
                System.out.println(href);
            }
            aElement.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hrefUrl;
    }


}
