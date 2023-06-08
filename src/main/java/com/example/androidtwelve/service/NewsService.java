package com.example.androidtwelve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.androidtwelve.domain.News;

import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【news】的数据库操作Service
* @createDate 2023-05-11 21:58:15
*/
public interface NewsService extends IService<News> {
    IPage<News> getpage(int currentPage, int pageSize, News news);
}
