package com.lyn.lost_and_found.crawl;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTest {

    public static String httpClientGetWebData(String url) {
        String entity = null;
        try {
            //初始化httpclient
//            HttpClient client = new DefaultHttpClient();
            HttpClient client = HttpClients.custom().build();
            //请求的地址URL
            String personalUrl = url;
            //  get方法请求
            HttpGet httpGet = new HttpGet(personalUrl);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpGet.setHeader("Cache-Control", "max-age=0");
            httpGet.setHeader("Host", "www.w3school.com.cn");
            //这项内容很重要
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            //初始化HTTP响应
//            HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
//                    HttpStatus.SC_OK, "OK");
            //执行响应
            HttpResponse response = client.execute(httpGet);
            //响应状态
            String status = response.getStatusLine().toString();
            //获取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            //协议的版本号
            ProtocolVersion protocolVersion = response.getProtocolVersion();
            //是否ok
            String phrase = response.getStatusLine().getReasonPhrase();
            //状态码200表示响应成功
            if (statusCode == 200) {
                //获取实体内容,这里为HTML内容
                entity = EntityUtils.toString(response.getEntity(), "gbk");
                //输出实体内容
//                System.out.println(entity);
                //消耗实体
                EntityUtils.consume(response.getEntity());
            } else {
                //关闭HttpEntity的流实体
                //消耗实体
                EntityUtils.consume(response.getEntity());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return entity;
    }

    public static void jsoupParseWebData(String html) {
        List<String> hrefUrl = new ArrayList<>();
        String prefix = "http://www.cswzl.com";
        Document document = Jsoup.parse(html);
        Element element = document.select("form#lostinfo").get(0).select(".info_title1").get(0);
//            System.out.println(element);i
        Elements aElement = element.select("a");
        System.out.println(aElement);
        for (Element a : aElement) {
            String href = prefix + a.attr("href");
            hrefUrl.add(href);
            System.out.println(href);
        }
//        Element element = document.select("div[class=div_body]").select("tr").get(4).select("tr").get(3);
//        String descripInfo = element.select("td").get(1).text();
//        System.out.println("描述信息：" + descripInfo);
    }

    public static void main(String[] args) {
//        String url = "http://www.cswzl.com/lostinfo.action";
        String url = "http://www.cswzl.com/lostinfo.action?q.currentPageNum=3&q.t=1&q.altercond=false";
        String html = httpClientGetWebData(url);
        jsoupParseWebData(html);
    }
}
