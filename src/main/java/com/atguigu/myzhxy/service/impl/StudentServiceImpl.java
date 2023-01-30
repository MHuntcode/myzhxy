package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.StudentMapper;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//ServiceImpl<AdminMapper, Admin> 的两个参数的第一个参数相当于注入private AdminMpper adminmapper语句
//Service("adminServiceImpl")加括号里面的东西，即可表明AdminServiceImpl类可以通过adminServiceImpl对象来获取，
//数据控制
@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getPassword());
        queryWrapper.eq("password",MD5.encrypt(loginForm.getPassword()));
        Student student=baseMapper.selectOne(queryWrapper);

        return student;
    }
}
