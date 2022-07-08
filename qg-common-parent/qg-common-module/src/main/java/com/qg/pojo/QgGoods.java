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
public class QgGoods implements Serializable {
    //主键
    private String id;
    //商品图片
    private String goodsImg;
    //商品名称
    private String goodsName;
    //商品单价
    private Double price;
    //原始库存
    private Integer stock;
    //
    private Date createdTime;
    //
    private Date updatedTime;

}
