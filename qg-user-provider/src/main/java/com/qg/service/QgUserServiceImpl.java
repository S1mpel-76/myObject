package com.qg.service;

import com.qg.mapper.QgUserMapper;
import com.qg.pojo.QgUser;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QgUserServiceImpl implements QgUserService {

    @Resource
    private QgUserMapper qgUserMapper;

    @Override
    public QgUser getQgUserById(String id) throws Exception {
        return qgUserMapper.getQgUserById(id);
    }

    @Override
    public List<QgUser> getQgUserListByMap(Map<String, Object> param) throws Exception {
        return qgUserMapper.getQgUserListByMap(param);
    }

    @Override
    public Integer getQgUserCountByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxAddQgUser(QgUser qgUser) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxModifyQgUser(QgUser qgUser) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxDeleteQgUserById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgUser(String ids) throws Exception {
        return null;
    }

    @Override
    public QgUser queryQgUserByPhoneAndPwd(String phone, String pwd) throws Exception {
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("phone",phone);
        param.put("password",pwd);
        List<QgUser> map = qgUserMapper.getQgUserListByMap(param);
        if(map!=null&&map.size()==1){
            return map.get(0);
        }
        return map.get(0);
    }
}
