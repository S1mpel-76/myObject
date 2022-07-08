package com.qg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.qg.common.Constants;
import com.qg.dto.ReturnResult;
import com.qg.dto.ReturnResultUtils;
import com.qg.pojo.QgGoods;
import com.qg.pojo.QgGoodsTempStock;
import com.qg.pojo.QgOrder;
import com.qg.pojo.QgUser;
import com.qg.utils.ActiveMqUtils;
import com.qg.utils.EmptyUtils;
import com.qg.utils.IdWorker;
import com.qg.utils.RedisUtil;
import com.qg.vo.GetGoodsMessage;
import com.qg.vo.GoodsVo;
import io.swagger.models.auth.In;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LocalQgGoodsServiceImpl implements LocalQgGoodsService {
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
    private ActiveMqUtils activeMqUtils;


    //启动一个监听抢购队列的线程
    @JmsListener(destination = Constants.ActiveMQMessage.getMessage)
    public void getGoods(GetGoodsMessage goodsMessage) throws Exception {
        String goodsId=goodsMessage.getGoodsId();
        String userId=goodsMessage.getUserId();
        String key=Constants.goodsPrefix+goodsId;
        //1:获取锁
        String keyLock=Constants.lockPrefix+goodsId;
        while (!redisUtil.lock(keyLock,30L)){
            TimeUnit.SECONDS.sleep(1);
        }
        //2:判断用户是否已经抢购，未支付或支付成功就不可以抢购
        String keyGetGoods=Constants.getGoodsPrefix+goodsId+":"+userId;
        String flag= redisUtil.getStr(keyGetGoods);
        if(EmptyUtils.isNotEmpty(flag) && "1102".equals(flag)){
            redisUtil.setStr(keyGetGoods,"1102");
            //释放锁
            redisUtil.unLock(keyLock);
            return;
            //已经抢购过了，不可以再抢了
            //return ReturnResultUtils.returnFail(1102,"您已经抢购过了，请去订单中查看");
        }
        //3:判断库存是否大于0
        String goodsVoJson=redisUtil.getStr(key);
        GoodsVo goodsVo = objectMapper.readValue(goodsVoJson, GoodsVo.class);
        Integer currentStock=goodsVo.getCurrentStock();
        if(currentStock<=0){
            redisUtil.setStr(keyGetGoods,"1103");
            //释放锁
            redisUtil.unLock(keyLock);
            return;
            //库存不足
            //return ReturnResultUtils.returnFail(1103,"商品已经抢空了，您可以参加下一轮");
        }
        //4:更新库存，更新临时库存插入数据，更新redis数据
        //更新redis数据
        goodsVo.setCurrentStock(goodsVo.getCurrentStock()-1);
        //写入redis
        redisUtil.setStr(key,objectMapper.writeValueAsString(goodsVo));
        //数据库新增
        QgGoodsTempStock qgGoodsTempStock=new QgGoodsTempStock();
        qgGoodsTempStock.setUserId(userId);
        qgGoodsTempStock.setGoodsId(goodsId);
        qgGoodsTempStock.setStatus(Constants.StockStatus.lock);
        qgGoodsTempStock.setCreatedTime(new Date());
        //如何获取自增的id
        Integer id= qgGoodsTempStockService.qdtxAddQgGoodsTempStock(qgGoodsTempStock);
        String stockId=id+"";
        //5:生成订单
        this.creatOrder(userId,goodsId,stockId);
        //6:缓存抢购的标识
        redisUtil.setStr(keyGetGoods,"1102");
        //7:释放锁
        redisUtil.unLock(keyLock);


    }

    @Override
    public ReturnResult<GoodsVo> qureyGoodsByMap(String goodsId) throws Exception {
        //从redis缓存中查询

        String key= Constants.goodsPrefix+goodsId;
        System.out.println(key);
        GoodsVo goodsVo=null;
        String goodsVoJson = redisUtil.getStr(key);
        if(EmptyUtils.isNotEmpty(goodsVoJson)){
            //如果redis里面已经存在缓存就直接序列化
            goodsVo = objectMapper.readValue(goodsVoJson, GoodsVo.class);
        }else{
            //如果不存在，生成vo对象写入reids
            goodsVo=new GoodsVo();
            QgGoods qgGoodsById = qgGoodsService.getQgGoodsById(goodsId);
            //属性拷贝
            BeanUtils.copyProperties(qgGoodsById,goodsVo);
            //计算当前库存
            Map<String,Object> param=new HashMap<String, Object>();
            param.put("goodsId",goodsId);
            param.put("status",1);
            Integer count = qgGoodsTempStockService.getQgGoodsTempStockCountByMap(param);
            goodsVo.setCurrentStock(qgGoodsById.getStock()-count);
            redisUtil.setStr(key,objectMapper.writeValueAsString(goodsVo));
        }
        return ReturnResultUtils.returnSuccess(goodsVo);
    }

    /**
     * 处理抢购业务（消息中间件）
     *
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public ReturnResult getGoodsByMessage(String goodsId, String token) throws Exception {
        //抢购前的验证用户的登录状态
        String json=redisUtil.getStr(token);
        String key=Constants.goodsPrefix+goodsId;
        if(EmptyUtils.isEmpty(json)){
            return ReturnResultUtils.returnFail(2011,"进入抢购区必须先登录");
        }
        //反序列化获取用户id
        QgUser user = objectMapper.readValue(json, QgUser.class);
        String userid=user.getId();
        String queueName=Constants.ActiveMQMessage.getMessage;
        //接受用户请求，把抢购对象放入消息中间件
        GetGoodsMessage goodsMessage=new GetGoodsMessage();
        goodsMessage.setUserId(userid);
        goodsMessage.setGoodsId(goodsId);
        goodsMessage.setCreatedDate(new Date());
        activeMqUtils.sendMessageToMQ(queueName,goodsMessage);
        //返回成功的处理结果
        return ReturnResultUtils.returnSuccess();
    }

    /**
     * 轮询
     *
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public ReturnResult flushResult(String goodsId, String token) throws Exception {
        String json=redisUtil.getStr(token);
        //反序列化获取用户id
        QgUser user = objectMapper.readValue(json, QgUser.class);
        String userId=user.getId();
        String keyGetGoods=Constants.getGoodsPrefix+goodsId+":"+userId;
        if(EmptyUtils.isNotEmpty(keyGetGoods)){
            String flag = redisUtil.getStr(keyGetGoods);
            if("1102".equals(flag)){
                return ReturnResultUtils.returnFail(1102,"您已经抢购成功，请去订单中查看");
            }else if("1103".equals(flag)){
                return ReturnResultUtils.returnFail(1103,"商品已经抢空了，您可以参加下一轮");
            }
        }
        return ReturnResultUtils.returnSuccess();
    }

    @Override
    public ReturnResult findGoodsById(String id) throws Exception {
        QgGoods goods = qgGoodsService.getQgGoodsById(id);
        return ReturnResultUtils.returnSuccess(goods);
    }

    public void creatOrder(String userId,String goodsId,String stockId)throws Exception{
        QgOrder qgOrder=new QgOrder();
        qgOrder.setUserId(userId);
        qgOrder.setGoodsId(goodsId);
        //雪花算法实现分布式下的唯一订单单号
        qgOrder.setOrderNo(IdWorker.getId());
        qgOrder.setCreatedTime(new Date());
        qgOrder.setNum(1);
        qgOrder.setStatus(Constants.OrderStatus.toPay);
        //计算订单的总价
        String goodsVoJson= redisUtil.getStr(Constants.goodsPrefix+goodsId);
        GoodsVo goodsVo=objectMapper.readValue(goodsVoJson,GoodsVo.class);
        qgOrder.setAmount(goodsVo.getPrice());
        qgOrder.setStockId(stockId);
        qgOrderService.qdtxAddQgOrder(qgOrder);
    }
}
