package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.utils.CreateVerifiCodeImage;
import com.atguigu.myzhxy.utils.JwtHelper;
import com.atguigu.myzhxy.utils.Result;
import jdk.nashorn.internal.ir.ReturnNode;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms/system")
public class SystemController {
     @Autowired
     private  AdminService adminService;
     @Autowired
     private  TeacherService teacherService;
     @Autowired
     private  StudentService studentService;
//因为前面请求发过来的是一个字符串，为了士气转化为LoginForm类型，所以加上@RequestBody LoginForm loginForm
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码的接收
        HttpSession session=request.getSession();
        String sessionVerifiCode=(String)session.getAttribute("verifiCode");
        String loginVerifiCode=loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null==sessionVerifiCode) {
            return Result.fail().message("验证码失败，请刷新重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return  Result.fail().message("验证码有误，请重新输入");
        }
        //从session域中移除现有的验证码
        session.removeAttribute("verifiCode");

        //分类用户进行校验
        //准备一个map用户存放响应的数据
        Map<String,Object> map=new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin= adminService.login(loginForm);
                    if (admin != null) {
                        //用户的类型和用户的id转化为一个密文，以token的名称向用户反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(),1));
                    }else {
                        throw new RuntimeException("用户密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return  Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student=studentService.login(loginForm);
                    if (student != null) {
                        map.put("token",JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw  new RuntimeException("用户密码错误");
                    }
                    return  Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return  Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher= teacherService.login(loginForm);
                    if (teacher != null) {
                        map.put("token",JwtHelper.createToken(teacher.getId().longValue(),3));
                    }else {
                        throw  new RuntimeException("用户密码错误");
                    }
                    return  Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return  Result.fail().message(e.getMessage());
                }
        }
      return  Result.fail().message("查无此用户");
    }
    @GetMapping("/getVerifiCodeImage")
    public  void  getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage= CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上传的验证码
        String verifiCode=new String((CreateVerifiCodeImage.getVerifiCode()));
        //将验证码文本放入session域，为下一次验证作准本
       HttpSession session= request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器

        try {
            ImageIO.write(verifiCodeImage,"JPG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
