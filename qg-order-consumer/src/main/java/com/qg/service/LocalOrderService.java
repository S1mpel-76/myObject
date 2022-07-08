package com.qg.service;

import com.qg.dto.ReturnResult;

public interface LocalOrderService {

   ReturnResult queryOrderList(String id, String token)throws Exception;

   ReturnResult prepay(String orderId,String token)throws Exception;
}
