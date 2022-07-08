package com.qg.service;

import com.alibaba.fastjson.JSONObject;
import com.qg.dto.ReturnResult;
import com.qg.dto.ReturnResultUtils;
import com.qg.pojo.QgUser;
import com.qg.utils.EmptyUtils;
import com.qg.utils.RedisUtil;
import com.qg.vo.QgTokenVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocalUserServiceImpl implements LocalUserService {
    @Reference
    private QgUserService qgUserService;
    @Resource
    private TokenService tokenService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 登录验证
     * @param phone
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public ReturnResult doLogin(String phone, String password) throws Exception {
        //1.去Redis缓存中判断用户是否登录
        String key="isLogin:"+phone;
        String result=redisUtil.getStr(key);
        System.out.println("==========>手机 "+phone);
        System.out.println("==========>密码 "+password);
        if(EmptyUtils.isNotEmpty(result)&&"1".equals(result)){
            return ReturnResultUtils.returnFail(10008,"您已经登陆过了");
        }
        //2：数据库验证用户名和密码是否正确
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("phone",phone);
        List<QgUser> qgUserListByMap = qgUserService.getQgUserListByMap(params);
//        password= MD5.getMd5(password,32);
//        System.out.println(password);
        QgUser user=qgUserListByMap.get(0);
        if(EmptyUtils.isNotEmpty(user)&&password.equals(user.getPassword())){
            //用户名和密码正确
            //3:根据用户的信息生成登录系统的令牌（Token）
            String token=tokenService.generateToken(user);
            //4:将Token和用户信息存入Redis
            //将用户对象序列化Json字符串
            String userJson= JSONObject.toJSONString(user);
            //写入Redis缓存保存两个小时
            redisUtil.setStr(token,userJson,120L);
            //5.将Token信息返回给前端,前端将令牌信息写入cookie
            QgTokenVO tokenVO=new QgTokenVO();
            tokenVO.setToken(token);
            Long startTime=System.currentTimeMillis();
            //令牌的开始时间
            tokenVO.setGenTime(startTime);
            //令牌过期时间
            tokenVO.setExpTime(startTime+7200*1000);
            //6:缓存用户已经登录的标识
            redisUtil.setStr(key,"1",120L);
            return ReturnResultUtils.returnSuccess(tokenVO);
        }else{
            return ReturnResultUtils.returnFail(10006,"账号或密码错误");
        }
    }

    @Override
    public ReturnResult logOut(String token) throws Exception {

        //1.根据令牌获取value
        String userJson=redisUtil.getStr(token);
        //2.将userJson字符串转换为Java对象
        QgUser qgUser= JSONObject.parseObject(userJson,QgUser.class);
        //3.删除用户的登录标识
        String key="isLogin:"+qgUser.getPhone();
        redisUtil.del(key);
        redisUtil.del(token);
        return ReturnResultUtils.returnSuccess("注销成功");
    }
}
