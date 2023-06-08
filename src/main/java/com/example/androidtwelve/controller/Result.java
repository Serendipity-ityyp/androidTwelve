package com.example.androidtwelve.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private int code;//成功失败标识
    private String msg;//相应的信息
    private Long total;//总的记录数
    private Object data;//返回的数据

    public static Result fail(){
        return result(400,"失败",0L,null);
    }

    public static Result suc(){
        return result(200,"成功",0L,null);
    }

    public static Result suc(Object data){
        return result(200,"成功",0L,data);
    }

    public static Result suc(Long total,Object data){
        return result(200,"成功",total,data);
    }

    private static Result result(int code,String msg,Long total,Object data){
        Result result = new Result(code, msg, total, data);
        return result;
    }
}
