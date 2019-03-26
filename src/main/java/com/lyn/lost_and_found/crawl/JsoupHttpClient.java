package com.lyn.lost_and_found.crawl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 1、使用 HttpClient 获取网页文档对象
 * 2、使用 Jsoup 获取所需的信息
 * 3、注意事项：
 * ① 设置连接超时：其实就是让它不再继续，做无意义的尝试
 * ② 爬虫时被屏蔽了，就更换代理 IP
 * ③ 有些网站是设置只能浏览器才能访问的，这时候就要模拟浏览器
 * ④ 有些网站的编码不一定是UTF-8，也有可能是GBK
 *
 * @author linhongcun
 */
public class JsoupHttpClient {
    public static void main(String[] args) throws ClientProtocolException, IOException {
        // ============================= 【HttpClient】 ====================================
        // 创建httpclient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httpget实例
        HttpGet httpget = new HttpGet("https://www.csdn.net//");

        // 模拟浏览器 ✔
        httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");

        // 使用代理 IP ✔
        HttpHost proxy = new HttpHost("39.134.67.78", 8080);
        RequestConfig config = RequestConfig.custom().setProxy(proxy)
                //设置连接超时 ✔
                .setConnectTimeout(10000) // 设置连接超时时间 10秒钟
                .setSocketTimeout(10000) // 设置读取超时时间10秒钟
                .build();

        httpget.setConfig(config);

        // 执行get请求
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        // 获取返回实体
        String content = EntityUtils.toString(entity, "utf-8");

        // ============================= 【Jsoup】 ====================================
        //获取响应类型、内容
        System.out.println("Status:" + response.getStatusLine().getStatusCode());
        System.out.println("Content-Type:" + entity.getContentType().getValue());
        // 解析网页 得到文档对象
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByTag("a"); // 获取tag是a的所有DOM元素，数组

        System.out.println(elements);
//        for (int i = 0; i < 17; ++i) {
//            Element element = elements.get(i); // 获取第i个元素
//            String title = element.text(); // 返回元素的文本
//            System.out.println("<a>：" + title);
//        }

        response.close(); // response关闭
        httpclient.close(); // httpClient关闭

    }

}


