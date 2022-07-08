package com.qg;

import com.qg.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class QgGoodsConsumerApplicationTests {
    @Resource
    private RedisUtil redisUtil;
    @Test
    void contextLoads() {

    }


}
