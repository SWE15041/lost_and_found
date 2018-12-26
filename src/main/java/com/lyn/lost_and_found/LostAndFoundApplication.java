package com.lyn.lost_and_found;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.uic.client.config.EnableAuthClient;
import com.jay.vito.website.EnableVitoWebServer;
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

        SpringApplication.run(LostAndFoundApplication.class, args);
    }

}




