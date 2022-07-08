package com.qg.service;

import com.qg.mapper.QgGoodsTempStockMapper;
import com.qg.pojo.QgGoodsTempStock;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class QgGoodsTempStockServiceImpl implements QgGoodsTempStockService {
    @Resource
    private QgGoodsTempStockMapper qgGoodsTempStockMapper;

    @Override
    public QgGoodsTempStock getQgGoodsTempStockById(String id) throws Exception {
        return qgGoodsTempStockMapper.getQgGoodsTempStockById(id);
    }

    @Override
    public List<QgGoodsTempStock> getQgGoodsTempStockListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getQgGoodsTempStockCountByMap(Map<String, Object> param) throws Exception {
        return qgGoodsTempStockMapper.getQgGoodsTempStockCountByMap(param);
    }

    @Override
    public Integer qdtxAddQgGoodsTempStock(QgGoodsTempStock qgGoodsTempStock) throws Exception {
        qgGoodsTempStockMapper.insertQgGoodsTempStock(qgGoodsTempStock);
        return Integer.valueOf(qgGoodsTempStock.getId());
    }

    @Override
    public Integer qdtxModifyQgGoodsTempStock(QgGoodsTempStock qgGoodsTempStock) throws Exception {
        return qgGoodsTempStockMapper.updateQgGoodsTempStock(qgGoodsTempStock);
    }

    @Override
    public Integer qdtxDeleteQgGoodsTempStockById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgGoodsTempStock(String ids) throws Exception {
        return null;
    }
}
