package com.atguigu.myzhxy.pojo;

import lombok.Data;

/**
 * @project: ssm_sms
 * @description: 用户登录表单信息
 */
//接受前端提交的数据（该实体类没有以之相对应的数据表）
@Data
public class LoginForm {

    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;

}
