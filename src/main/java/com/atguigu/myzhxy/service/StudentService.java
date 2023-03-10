package com.atguigu.myzhxy.service;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);
}
