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
public class QgGoodsTempStock implements Serializable {
    //主键
    private String id;
    //商品图片
    private String goodsId;
    //
    private String userId;
    //状态(0:锁定库存,1:成功支付,2:支付超时)
    private Integer status;
    //
    private Date createdTime;
    //
    private Date updatedTime;

}
