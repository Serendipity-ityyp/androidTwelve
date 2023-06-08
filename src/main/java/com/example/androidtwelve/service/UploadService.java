package com.example.androidtwelve.service;


import com.example.androidtwelve.controller.ResponseResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface UploadService  {
    ResponseResult uploadImg(MultipartFile img) throws Exception;
}
