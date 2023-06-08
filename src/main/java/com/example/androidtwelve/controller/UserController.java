package com.example.androidtwelve.controller;

import cn.hutool.crypto.SecureUtil;
import com.example.androidtwelve.domain.User;
import com.example.androidtwelve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Result login(String account,String password){
        List<User> list = userService.list();
        for (User user : list) {
            if (user.getAccount().equals(account) && user.getPassword().equals(SecureUtil.md5(password))){
                System.out.println("登陆成功");
                return Result.suc(user);
            }
        }
        return Result.fail();
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        user.setPassword(SecureUtil.md5(user.getPassword()));
        return Result.suc(userService.save(user));
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable long id){
        return Result.suc(userService.getById(id));
    }

    @PutMapping
    public Result updateUser(@RequestBody User user){
        user.setPassword(SecureUtil.md5(user.getPassword()));
        return Result.suc(userService.updateById(user));
    }

}
