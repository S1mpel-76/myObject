package com.qg.service;

import com.qg.mapper.QgGoodsMapper;
import com.qg.pojo.QgGoods;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.apache.dubbo.config.annotation.Service;
@Service
public class QgGoodsServiceImpl implements QgGoodsService {
    @Resource
    private QgGoodsMapper qgGoodsMapper;
    @Override
    public QgGoods getQgGoodsById(String id) throws Exception {
        return qgGoodsMapper.getQgGoodsById(id);
    }

    @Override
    public List<QgGoods> getQgGoodsListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getQgGoodsCountByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxAddQgGoods(QgGoods qgGoods) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxModifyQgGoods(QgGoods qgGoods) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxDeleteQgGoodsById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgGoods(String ids) throws Exception {
        return null;
    }
}
