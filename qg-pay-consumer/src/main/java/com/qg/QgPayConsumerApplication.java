package com.qg;

import com.alipay.api.AlipayConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDubbo
public class QgPayConsumerApplication {


    public static void main(String[] args) {
        SpringApplication.run(QgPayConsumerApplication.class, args);
    }

}
