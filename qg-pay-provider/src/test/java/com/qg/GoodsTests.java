package com.qg;

import com.qg.pojo.QgGoods;
import com.qg.service.QgGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class GoodsTests {
    @Resource
    private QgGoodsService qgGoodsService;
    @Test
    public void testGetGoods()throws Exception{
        QgGoods goods = qgGoodsService.getQgGoodsById("1");
        log.info("============>goods"+goods.getGoodsName());
    }
}
