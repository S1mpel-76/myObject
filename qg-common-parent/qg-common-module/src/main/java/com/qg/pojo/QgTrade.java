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
public class QgTrade implements Serializable {
    //
    private String id;
    //业务订单号
    private String orderNo;
    //交易订单号
    private String tradeNo;
    //(1:支付宝 2:微信)
    private Integer payMethod;
    //交易金额
    private Double amount;
    //
    private Date createdTime;
    //
    private Date updatedTime;

}
