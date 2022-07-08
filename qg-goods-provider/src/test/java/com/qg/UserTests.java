package com.qg;

import com.qg.pojo.QgUser;
import com.qg.service.QgUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class UserTests {
    @Resource
    private QgUserService qgUserService;

    @Test
    public void testGetQgUserById()throws Exception{
        QgUser user = qgUserService.getQgUserById("1");
        if(user!=null){
            log.info("++++++++++++++++>user-provider，读取用户信息"+user.getPhone());
        }
    }
}
