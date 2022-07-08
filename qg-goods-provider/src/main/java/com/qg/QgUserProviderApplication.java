package com.qg;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qg.mapper")
public class QgUserProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgUserProviderApplication.class, args);
    }

}
