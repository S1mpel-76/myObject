package com.qg.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qg.dto.ReturnResult;

import com.qg.pojo.QgOrder;
import com.qg.service.LocalPayService;
import com.qg.service.QgOrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller

public class QgPayController {
    @Resource
    private LocalPayService localPayService;

    @Reference
    private QgOrderService qgOrderService;

    /**
     * 支付宝支付请求
     * @param orderId
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("api/v/toPay")
    public void toPay(String orderId, String token, HttpServletResponse response)throws Exception{
        String form=localPayService.toPay(orderId,token);
        //4.将支付宝开发平台的支付表单相应给用户
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();

    }

    /**
     * //Get方式 同步通知（由浏览器发起）给买家看的
     * @param response
     * @param request
     * @throws Exception
     */
    @GetMapping("/api/v/callBack")
    public void callBack(HttpServletResponse response,HttpServletRequest request)throws Exception{
        boolean signVerified = localPayService.rsaCheckV1(request);
        if(signVerified){
            //商户订单号
            String orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //根据订单编号查询订单
            QgOrder qgOrder = qgOrderService.queryQgOrderByNo(orderNo);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            if(!localPayService.processed(orderNo)){
                //判断该订单是否已经处理
                localPayService.paySuccess(orderNo,tradeNo);
            }
            //返回支付成功的页面
            response.sendRedirect("http://localhost:8803/paySuccess.html?orderId="+qgOrder.getId());
        }else {
            //返回支付失败

        }
    }

    /**
     * 支付宝发起的异步请求（post）
     * @param response
     * @param request
     * @throws Exception
     */
    @PostMapping("api/v/notifyUrl")
    public void notifyUrl(HttpServletResponse response,HttpServletRequest request)throws Exception{
        boolean signVerified = localPayService.rsaCheckV1(request);

        if(signVerified){
            //订单状态修改
            //商户订单号
            String orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
            if(tradeStatus.equals("TRADE_FINISHED")){
                //执行订单业务
                //订单业务
                if(!localPayService.processed(tradeNo)){
                    //判断该订单是否已经处理
                    localPayService.paySuccess(orderNo,tradeNo);
                }
            }else if (tradeStatus.equals("TRADE_SUCCESS")){
                //订单业务
                if(!localPayService.processed(tradeNo)){
                    //判断该订单是否已经处理
                    localPayService.paySuccess(orderNo,tradeNo);
                }
            }
            //根据订单编号查询订单
            QgOrder qgOrder = qgOrderService.queryQgOrderByNo(orderNo);
            qgOrder.setStatus(1);
            Integer integer = qgOrderService.qdtxModifyQgOrder(qgOrder);

            if(integer>0){
                response.getWriter().write("success");
                response.getWriter().flush();
                response.getWriter().close();
            }else{
                response.getWriter().write("fail");
                response.getWriter().flush();
                response.getWriter().close();
            }

        }
    }

}
