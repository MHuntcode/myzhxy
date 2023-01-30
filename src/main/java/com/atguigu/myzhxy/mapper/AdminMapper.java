package com.atguigu.myzhxy.mapper;

import com.atguigu.myzhxy.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
//使得spring识别到AdminMapper接口，可加可不加，最好加上，以免出错，乱报红线
@Repository
public interface AdminMapper  extends BaseMapper<Admin> {

}
