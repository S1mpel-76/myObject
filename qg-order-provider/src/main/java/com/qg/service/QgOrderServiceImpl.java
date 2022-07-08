package com.qg.service;

import com.qg.mapper.QgOrderMapper;
import com.qg.pojo.QgOrder;
import org.apache.dubbo.config.annotation.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QgOrderServiceImpl implements QgOrderService {
    @Resource
    private QgOrderMapper qgOrderMapper;
    public QgOrder getQgOrderById(String id)throws Exception{
        return qgOrderMapper.getQgOrderById(id);
    }

    public List<QgOrder>	getQgOrderListByMap(Map<String,Object> param)throws Exception{
        return qgOrderMapper.getQgOrderListByMap(param);
    }

    public Integer getQgOrderCountByMap(Map<String,Object> param)throws Exception{
        return qgOrderMapper.getQgOrderCountByMap(param);
    }

    public Integer qdtxAddQgOrder(QgOrder qgOrder)throws Exception{
        //qgOrder.setCreatedTime(new Date());
        return qgOrderMapper.insertQgOrder(qgOrder);
    }

    public Integer qdtxModifyQgOrder(QgOrder qgOrder)throws Exception{
        //qgOrder.setUpdatedTime(new Date());
        return qgOrderMapper.updateQgOrder(qgOrder);
    }

    public Integer qdtxDeleteQgOrderById(String id)throws Exception{
        return qgOrderMapper.deleteQgOrderById(id);
    }

    public Integer qdtxBatchDeleteQgOrder(String ids)throws Exception{
        Map<String,List<String>> param=new HashMap<String,List<String>>();
        String[] paramArrays=ids.split(",");
        List<String> idList=new ArrayList<String>();
        for (String temp:paramArrays){
            idList.add(temp);
        }
        param.put("ids",idList);
        return qgOrderMapper.batchDeleteQgOrder(param);
    }

    @Override
    public QgOrder queryQgOrderByNo(String orderNo) throws Exception {
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("orderNo",orderNo);

        List<QgOrder> qgOrderList=qgOrderMapper.getQgOrderListByMap(param);
        if(qgOrderList!=null){
            return qgOrderList.get(0);
        }
        return null;
    }
}
