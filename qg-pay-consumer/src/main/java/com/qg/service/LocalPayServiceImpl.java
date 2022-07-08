package com.qg.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.common.Constants;

import com.qg.dto.ReturnResult;

import com.qg.pojo.QgGoods;
import com.qg.pojo.QgGoodsTempStock;
import com.qg.pojo.QgOrder;
import com.qg.pojo.QgTrade;
import com.qg.utils.EmptyUtils;
import com.qg.utils.RedisUtil;
import com.qg.vo.GoodsVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class LocalPayServiceImpl implements LocalPayService {
    @Reference
    private QgTradeService qgTradeService;
    @Reference
    private QgOrderService qgOrderService;
    @Reference
    private QgGoodsService qgGoodsService;
    @Reference
    private QgGoodsTempStockService qgGoodsTempStockService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AlipayConfig alipayConfig;

    /**
     * 验签
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public boolean rsaCheckV1(HttpServletRequest request) throws Exception {
        //获取支付宝GET过来反馈信息

        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //2.验证参数是否正确
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType()); //调用SDK验证签名
        return signVerified;
    }

    /**
     * 判断订单是否处理
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Override
    public boolean processed(String orderNo) throws Exception {
        //根据订单号获取订单信息
        QgOrder qgOrder = qgOrderService.queryQgOrderByNo(orderNo);
        //状态（0:待支付，1：成功，2：失败）
        if(EmptyUtils.isNotEmpty(qgOrder)&&"1".equals(qgOrder.getStatus())){
            return true;
        }
        return false;
    }

    @Override
    public void paySuccess(String orderNo, String tradeNo) throws Exception {
        //更新订单的状态
        QgOrder qgOrder = qgOrderService.queryQgOrderByNo(orderNo);
        qgOrder.setStatus(1);
        qgOrderService.qdtxModifyQgOrder(qgOrder);
        //更新购买记录表中的状态
        QgGoodsTempStock stock = qgGoodsTempStockService.getQgGoodsTempStockById(qgOrder.getStockId());
        stock.setStatus(1);
        qgGoodsTempStockService.qdtxModifyQgGoodsTempStock(stock);
        //插入一条支付记录
        QgTrade qgTrade=new QgTrade();
        qgTrade.setOrderNo(orderNo);
        qgTrade.setTradeNo(tradeNo);
        qgTrade.setAmount(qgOrder.getAmount());
        qgTrade.setPayMethod(1);
        qgTrade.setCreatedTime(new Date());
        qgTradeService.qdtxAddQgTrade(qgTrade);
    }

    /**
     * 支付宝支付
     * @param orderId
     * @param token
     * @throws Exception
     */
    @Override
    public String toPay(String orderId, String token) throws Exception {
        //根据订单id获取订单对象
        QgOrder qgOrder = qgOrderService.getQgOrderById(orderId);
        String orderNo=qgOrder.getOrderNo();
        String amount=qgOrder.getAmount().toString();
        //根据订单获取商品的信息从reids
        String qgGoodsJson = redisUtil.getStr(Constants.goodsPrefix + qgOrder.getGoodsId());
        String subject="";
        QgGoods qgGoods=null;
        if(EmptyUtils.isNotEmpty(qgGoodsJson)){
             qgGoods= objectMapper.readValue(qgGoodsJson, QgGoods.class);
        }else{
            qgGoods = qgGoodsService.getQgGoodsById(qgOrder.getGoodsId());
        }
        subject=qgGoods.getGoodsName();


        //获得初始化的AlipayClient

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getServerUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", alipayConfig.getCharset(), alipayConfig.getAlipayPublicKey(), alipayConfig.getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        String notifyUrl = "http://192.168.40.130:8088/api/v/notifyUrl";//异步通知网站（必须是上线的网站）
        String returnUrl = "http://192.168.40.130:8088/api/v/callBack";//同步通知网址
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderNo + "\","
                + "\"total_amount\":\"" + amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //返回请求
       return alipayClient.pageExecute(alipayRequest).getBody();
    }
}
