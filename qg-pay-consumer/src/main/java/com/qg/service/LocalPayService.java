package com.qg.service;

import com.qg.dto.ReturnResult;
import org.springframework.mail.MailParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LocalPayService {

    String toPay(String orderId, String token)throws Exception;

    /**
     * 验签
     * @param request
     * @return
     * @throws Exception
     */
    boolean rsaCheckV1(HttpServletRequest request)throws Exception;

    /**
     * 判断订单是否处理
     * @param orderNo
     * @return
     * @throws Exception
     */
    boolean processed(String orderNo) throws Exception;

    void paySuccess(String orderNo,String tradeNo)throws Exception;

}
