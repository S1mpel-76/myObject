package com.qg.vo;

import java.io.Serializable;
import java.util.Date;

/***
 * 抢购消息类
 */
public class GetGoodsMessage implements Serializable{

    private String userId;

    private String goodsId;

    private Date createdDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
