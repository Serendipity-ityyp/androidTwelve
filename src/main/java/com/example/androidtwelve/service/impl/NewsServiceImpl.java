package com.example.androidtwelve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.androidtwelve.domain.News;
import com.example.androidtwelve.mapper.NewsMapper;
import com.example.androidtwelve.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【news】的数据库操作Service实现
* @createDate 2023-05-11 21:58:15
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Override
    public IPage<News> getpage(int currentPage, int pageSize, News news) {
        LambdaQueryWrapper<News> lqw = new LambdaQueryWrapper<>();
//        lqw.like(Strings.isNotEmpty(news.getName()), News::getName, news.getName());
//        lqw.like(Strings.isNotEmpty(news.getTelphone()), Contacts::getTelphone, news.getTelphone());
//        lqw.like(Strings.isNotEmpty(news.getAddress()), Contacts::getAddress, news.getAddress());

        IPage page = new Page(currentPage,pageSize);
        newsMapper.selectPage(page,lqw);
        return page;
    }
}




