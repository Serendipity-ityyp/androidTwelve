package com.example.androidtwelve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.example.androidtwelve.domain.News;
import com.example.androidtwelve.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping()
    public Result getAll(){
        return Result.suc(newsService.list());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id){
        return Result.suc(newsService.getById(id));
    }

    @PostMapping
    public Result add(@RequestBody News news){
        return Result.suc(newsService.save(news));
    }

    @PutMapping
    public Result updateById(@RequestBody News news){
        return Result.suc(newsService.updateById(news));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        return Result.suc(newsService.removeById(id));
    }

    @GetMapping("{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize, News news){
        IPage<News> page = newsService.getpage(currentPage, pageSize, news);
        if (currentPage > page.getPages()){
            page = newsService.getpage((int)page.getPages(),pageSize, news);
        }
        return Result.suc(page.getTotal(),page);
    }
}
