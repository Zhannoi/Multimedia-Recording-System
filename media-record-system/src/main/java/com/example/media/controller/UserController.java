package com.example.media.controller;

import com.example.media.entity.User;
import com.example.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制层（提供接口）
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        String msg = userService.register(user);
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", msg);
        return res;
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        User loginUser = userService.login(user.getUsername(), user.getPassword());
        Map<String, Object> res = new HashMap<>();

        if (loginUser == null) {
            res.put("code", 400);
            res.put("data", null);
            res.put("msg", "用户名或密码错误");
        } else {
            res.put("code", 200);
            res.put("data", loginUser);  // 前端可以存 token 或 id
            res.put("msg", "登录成功");
        }

        return res;
    }
}