package com.qg.controller;



import com.qg.common.Constants;
import com.qg.dto.ReturnResultUtils;
import com.qg.service.LocalUserService;
import com.qg.dto.ReturnResult;
import com.qg.utils.EmptyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
//@CrossOrigin({"http://localhost:8803"})
@RequestMapping("/api")
public class UserController {
    @Resource
    private LocalUserService localUserService;

    @PostMapping("/doLogin")
    public ReturnResult doLogin(String phone,String password)throws Exception{
        return localUserService.doLogin(phone,password);
    }
    @PostMapping  ("/v/loginOut")
    public ReturnResult loginOut(String token)throws Exception{
        //2.删除Reids缓存中存储的令牌
        return localUserService.logOut(token);
    }

}
