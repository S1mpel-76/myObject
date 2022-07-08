package com.qg.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/***
*   用户表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QgUser implements Serializable {
    //主键
    private String id;
    //手机号
    private String phone;
    //密码
    private String password;
    //
    private String wxUserId;
    //真实姓名
    private String realName;
    //创建时间
    private Date createdTime;
    //更新时间
    private Date updatedTime;

}
