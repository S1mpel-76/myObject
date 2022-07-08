package com.qg;

import com.qg.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class QgPayConsumerApplicationTests {
    @Resource
    private RedisUtil redisUtil;
    @Test
    void contextLoads() {

    }


}
