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
public class QgGoodsMessage implements Serializable {
    //主键
    private String id;
    //用户ID
    private String userId;
    //消息ID
    private String goodsId;
    //抢购状态
    private String status;
    //金额
    private Double amount;
    //创建时间
    private Date createdTime;
    //更新时间
    private Date updatedTime;

}
