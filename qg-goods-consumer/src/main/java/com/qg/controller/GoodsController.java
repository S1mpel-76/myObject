package com.qg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.common.Constants;
import com.qg.dto.ReturnResult;
import com.qg.dto.ReturnResultUtils;
import com.qg.pojo.QgGoods;
import com.qg.service.LocalQgGoodsService;
import com.qg.service.QgGoodsService;
import com.qg.service.QgGoodsTempStockService;
import com.qg.utils.EmptyUtils;
import com.qg.utils.RedisUtil;
import com.qg.vo.GoodsVo;
import io.swagger.annotations.ResponseHeader;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class GoodsController {
    @Reference
    private QgGoodsService qgGoodsService;
    @Reference
    private QgGoodsTempStockService qgGoodsTempStockService;
    @Resource
    private LocalQgGoodsService localQgGoodsService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ObjectMapper objectMapper;


    @RequestMapping("v/queryGoodsById")
    public ReturnResult queryGoodsById(String id,String token)throws Exception{
        //抢购前的验证用户的登录状态
        String userJson=redisUtil.getStr(token);
        if(EmptyUtils.isEmpty(userJson)){
            return ReturnResultUtils.returnFail(2001,"进入抢购区必须先登录");
        }
        GoodsVo goodsVo=null;
        //去redis缓存中查询是否存在商品信息+实际库存信息
        String key= Constants.goodsPrefix+id;
        String goodsVoJson= redisUtil.getStr(key);
        if(EmptyUtils.isNotEmpty(goodsVoJson)){
            //将Json字符串，转换java对象
            goodsVo = objectMapper.readValue(goodsVoJson, GoodsVo.class);
        }else{

            goodsVo=new GoodsVo();
            //如果redis中不存在,进入数据区计算当前库存
            //查询出原始库存
            QgGoods qgGoods = qgGoodsService.getQgGoodsById(id);
            Integer stock=qgGoods.getStock();
            //查询出以销售的库存
            Map<String,Object> param=new HashMap<String, Object>();
            param.put("goodsId",id);
            param.put("active",1);
            Integer count = qgGoodsTempStockService.getQgGoodsTempStockCountByMap(param);
            //当前库存=原始-销售
           //写入缓存中
            goodsVo.setCurrentStock(stock-count);
            //属性拷贝
            BeanUtils.copyProperties(qgGoods,goodsVo);
            redisUtil.setStr(key,objectMapper.writeValueAsString(goodsVo));

        }

        return ReturnResultUtils.returnSuccess(goodsVo);
    }


    /**
     * 处理抢购请求
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("v/getGoods")
    public ReturnResult getGoodsByMessage(String goodsId,String token)throws Exception{
        return  localQgGoodsService.getGoodsByMessage(goodsId,token);
    }

    /**
     * 轮询
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    @PostMapping("/v/flushResult")
    public ReturnResult flushResult(String goodsId,String token)throws Exception {
       return localQgGoodsService.flushResult(goodsId,token);

    }
}
