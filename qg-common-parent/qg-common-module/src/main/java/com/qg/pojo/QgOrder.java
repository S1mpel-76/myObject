package com.qg.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/***
*   
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QgOrder implements Serializable {
    //主键
    private String id;
    //
    private String userId;
    //库存Id
    private String stockId;
    //订单编号
    private String orderNo;
    //商品Id
    private String goodsId;
    //购买量
    private Integer num;
    //总价
    private Double amount;
    //状态(0：待支付 1：支付成功 2:支付失败)
    private Integer status;
    //阿里支付交易号
    private String aliTradeNo;
    //
    private Date createdTime;
    //
    private Date updatedTime;

}
