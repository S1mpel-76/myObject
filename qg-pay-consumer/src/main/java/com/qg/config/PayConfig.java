package com.qg.config;

import com.alipay.api.AlipayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:pay.properties")
public class PayConfig {
    @Value("${pay.appId}")
    private String appId;
    @Value("${pay.serverUrl}")
    private String serverUrl;
    @Value("${pay.privateKey}")
    private String privateKey;
    @Value("${pay.alipayPublicKey}")
    private String alipayPublicKey;

    @Bean
    public AlipayConfig alipayConfig(){
        AlipayConfig alipayConfig=new AlipayConfig();
        alipayConfig.setServerUrl(this.serverUrl);
        alipayConfig.setAppId(this.appId);
        alipayConfig.setPrivateKey(this.privateKey);
        alipayConfig.setAlipayPublicKey(this.alipayPublicKey);
        return alipayConfig;
    }



}
