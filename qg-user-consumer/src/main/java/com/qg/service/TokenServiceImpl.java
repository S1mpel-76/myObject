package com.qg.service;


import com.qg.common.Constants;
import com.qg.pojo.QgUser;
import com.qg.utils.DateUtil;
import com.qg.utils.MD5;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String generateToken(QgUser user) throws Exception {
        StringBuffer token=new StringBuffer("token");
        token.append(MD5.getMd5(user.getPhone(),16));
        token.append("-");
        token.append(user.getId());
        token.append("-");
        String time= DateUtil.format(new Date(),"yyyyMMddHHmmss");
        token.append(time);
        token.append("-");
        String rand=MD5.getMd5(String.valueOf(MD5.getRandomCode()),6);
        token.append(rand);
        return token.toString();
    }
}
