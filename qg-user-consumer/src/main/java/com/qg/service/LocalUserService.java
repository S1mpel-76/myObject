package com.qg.service;

import com.qg.dto.ReturnResult;

public interface LocalUserService {

  /**
   * 登录验证
   * @param phone
   * @param pwd
   * @return
   * @throws Exception
   */
  public  ReturnResult doLogin(String phone ,String pwd)throws Exception;

  public  ReturnResult logOut(String token)throws Exception;

}
