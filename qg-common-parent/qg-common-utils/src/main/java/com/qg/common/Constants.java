package com.qg.common;

/***
 * 常量类
 */
public class Constants {
    //登录超时时间 30分钟
    public static final Long loginExpire=30L;
    //锁的超时时间
    public static final Long lockExpire=60L;

    public static final String tokenPrefix="token:";

    public static final String goodsPrefix="goods:";

    public static final String getGoodsPrefix="getGoods:";

    public static final String lockPrefix="lock:";

    public final static Integer DECIMAL_DIGITS = 2;

    public static final class StockStatus{
        public static final Integer lock=0;
        public static final Integer paySuccess=1;
        public static final Integer payOverTime=2;
    }

    public static final class GetGoodsStatus{
        public static final String getSuccess="1";
        public static final String getFail="0";
    }

    /**
     * 消息传递定义的destination
     */
    public static final class ActiveMQMessage{
        //抢购的消息名称
        public static final String getMessage="GET:MESSAGE";
    }
    //订单状态 0：待支付 1：支付成功 2:支付失败
    public static final class OrderStatus{
        public static final Integer toPay=0;
        public static final Integer paySuccess=1;
        public static final Integer payFail=2;
    }

    public static final class PayMethod{
        public static final Integer aliPay=1;
        public static final Integer wxPay=2;

    }
}
