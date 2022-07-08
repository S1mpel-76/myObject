package com.qg.controller;

import com.qg.dto.ReturnResult;
import com.qg.service.LocalOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class QgOrderController {
    @Resource
    private LocalOrderService localOrderService;
    /**
     * 查询指定用户的订单列表
     * @param id
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("api/v/queryOrderList")
    public ReturnResult queryOrderList(String id,String token)throws Exception{
        return localOrderService.queryOrderList(id,token);
    }

    /**
     * 支付前显示的订单详情
     * @param orderId
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("api/v/prepay")
    public ReturnResult prepay(String orderId,String token)throws Exception{
            return localOrderService.prepay(orderId,token);
    }
}
