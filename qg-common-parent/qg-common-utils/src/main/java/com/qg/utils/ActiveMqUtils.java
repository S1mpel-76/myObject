package com.qg.utils;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.jms.Destination;

@Component
public class ActiveMqUtils {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessageToMQ(String ququeName,Object message){
        Destination destination=new ActiveMQQueue(ququeName);
        jmsMessagingTemplate.convertAndSend(destination,message);
    }
}
