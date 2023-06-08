package com.example.androidtwelve.mapper;

import com.example.androidtwelve.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-05-17 23:21:48
* @Entity com.example.androidtwelve.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




