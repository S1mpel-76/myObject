package com.qg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.common.Constants;
import com.qg.dto.ReturnResult;
import com.qg.dto.ReturnResultUtils;
import com.qg.pojo.QgGoods;
import com.qg.pojo.QgOrder;
import com.qg.pojo.QgUser;
import com.qg.utils.EmptyUtils;
import com.qg.utils.RedisUtil;
import com.qg.vo.GoodsVo;
import com.qg.vo.OrderVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocalOrderServiceImpl implements LocalOrderService{
    @Reference
    private QgOrderService qgOrderService;
   @Reference
   private QgGoodsService qgGoodsService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ObjectMapper objectMapper;

    @Override

    public ReturnResult prepay(String orderId, String token) throws Exception {
        String userJson=redisUtil.getStr(token);
        QgUser qgUser = objectMapper.readValue(userJson, QgUser.class);
        String userId=qgUser.getId();
        QgOrder qgOrder = qgOrderService.getQgOrderById(orderId);
        if(EmptyUtils.isNotEmpty(qgOrder)&& !userId.equals(qgOrder.getUserId())){
            System.out.println("========="+qgOrder.getUserId());
            System.out.println("============"+userId);
           return   ReturnResultUtils.returnFail(2001,"没有找到你的订单信息");
        }
        return ReturnResultUtils.returnSuccess(qgOrder);
    }

    @Override
    public ReturnResult queryOrderList(String id, String token) throws Exception {
        Map<String ,Object> map=new HashMap<String, Object>();
        map.put("userId",id);
        List<QgOrder> qgOrderList= qgOrderService.getQgOrderListByMap(map);
        List<OrderVo> orderVoList=new ArrayList<>();
        for(QgOrder qgorder:qgOrderList){
            OrderVo orderVo=new OrderVo();
            //属性拷贝
            BeanUtils.copyProperties(qgorder,orderVo);
            //设置订单中商品的图片信息
            String goodsVoJson= redisUtil.getStr(Constants.goodsPrefix+qgorder.getGoodsId());
            //判断缓存中是否存在该商品信息
            if(EmptyUtils.isNotEmpty(goodsVoJson)){
                GoodsVo goodsVo = objectMapper.readValue(goodsVoJson, GoodsVo.class);
                orderVo.setGoodsImg(goodsVo.getGoodsImg());

            }else{
                //远程调用goods-provider，来获取商品表中的商品图片的地址
                QgGoods qgGoods = qgGoodsService.getQgGoodsById(qgorder.getGoodsId());
                orderVo.setGoodsImg(qgGoods.getGoodsName());
            }
            orderVoList.add(orderVo);
        }
        return ReturnResultUtils.returnSuccess(orderVoList);
    }
}
