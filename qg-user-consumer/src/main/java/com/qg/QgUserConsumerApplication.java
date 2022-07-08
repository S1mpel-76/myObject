package com.qg;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo

public class QgUserConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgUserConsumerApplication.class, args);
    }

}
