package com.qg.service;


import com.qg.pojo.QgUser;


public interface TokenService {
    /**
     * 跟据用户信息生成Token
     * @param user
     * @return
     */
    String generateToken(QgUser user) throws  Exception;
}
