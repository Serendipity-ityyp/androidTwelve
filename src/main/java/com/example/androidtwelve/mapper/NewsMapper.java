package com.example.androidtwelve.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.androidtwelve.domain.News;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【news】的数据库操作Mapper
* @createDate 2023-05-11 21:58:15
* @Entity com.example.domain.News
*/
@Mapper
public interface NewsMapper extends BaseMapper<News> {

}




