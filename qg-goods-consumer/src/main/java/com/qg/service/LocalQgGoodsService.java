package com.qg.service;

import com.qg.dto.ReturnResult;
import com.qg.pojo.QgGoods;
import com.qg.vo.GoodsVo;

public interface LocalQgGoodsService {
    public ReturnResult findGoodsById(String id)throws Exception;

    public ReturnResult<GoodsVo> qureyGoodsByMap(String id)throws Exception;

    /**
     * 处理抢购业务（消息中间件）
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    ReturnResult getGoodsByMessage(String goodsId,String token)throws Exception;

    /**
     * 轮询
     * @param goodsId
     * @param token
     * @return
     * @throws Exception
     */
    ReturnResult flushResult(String goodsId,String token)throws Exception;
}
