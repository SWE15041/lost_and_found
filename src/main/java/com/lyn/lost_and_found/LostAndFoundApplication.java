package com.lyn.lost_and_found;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.uic.client.config.EnableAuthClient;
import com.jay.vito.website.EnableVitoWebServer;
import com.jay.vito.website.core.cache.SystemDataHolder;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.util.exception.LoadModelException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement  // 开启事务
@EntityScan("com.lyn.lost_and_found")
@EnableJpaRepositories(basePackages = {"com.lyn.lost_and_found"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@SpringBootApplication(scanBasePackages = {"com.lyn.lost_and_found"})
@EnableVitoWebServer
@EnableAuthClient
public class LostAndFoundApplication {

    public static void main(String[] args) {
        System.setProperty("app.env", "dev");
        //分词模型初始化

        SpringApplication.run(LostAndFoundApplication.class, args);
        String path = SystemDataHolder.getParam("modelsDir", String.class);
        try {
            CNFactory.getInstance(path);
            System.out.println("-------------------分词模型载入完成----------------------");
        } catch (LoadModelException e) {
            System.out.println("-------------------分词模型载入异常----------------------");
        }

    }

}




