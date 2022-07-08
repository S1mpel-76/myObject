package com.qg.controller;

import com.qg.dto.ReturnResult;
import com.qg.dto.ReturnResultUtils;
import com.qg.utils.ActiveMqUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MqController {
    @Resource
    private ActiveMqUtils activeMqUtils;

    //发送消息(消息的生产者)
    @GetMapping("api/send")
    public ReturnResult sendMessage()throws Exception{
        List<String> list=new ArrayList<String>();
        list.add("哈哈");
        list.add("喜喜");
        list.add("嘎嘎");
        activeMqUtils.sendMessageToMQ("mq:test",list);
        return ReturnResultUtils.returnSuccess();

    }
}
