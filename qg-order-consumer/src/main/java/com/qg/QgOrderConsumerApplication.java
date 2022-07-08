package com.qg;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import java.util.List;

@SpringBootApplication
@EnableDubbo
public class QgOrderConsumerApplication {
    //启动一个监听线程，监听 mq:test队列上的消息
    @JmsListener(destination = "mq:test")
    public void receiveMessage(List<String> message){
        System.out.println("=============>监听到队列上的消息："+message);
    }

    public static void main(String[] args) {
        SpringApplication.run(QgOrderConsumerApplication.class, args);
    }

}
