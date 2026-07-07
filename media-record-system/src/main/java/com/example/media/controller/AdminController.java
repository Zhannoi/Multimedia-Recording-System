package com.example.media.controller;

import com.example.media.entity.Admin;
import com.example.media.service.AdminService;
import com.example.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    //  管理员登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Admin admin) {

        Map<String, Object> result = new HashMap<>();

        Admin a = adminService.login(admin.getUsername(), admin.getPassword());

        if (a != null) {
            result.put("code", 200);
            result.put("data", a);
        } else {
            result.put("code", 500);
            result.put("msg", "账号或密码错误");
        }

        return result;
    }

    //  获取所有用户
    @GetMapping("/users")
    public Map<String, Object> getUsers() {

        Map<String, Object> result = new HashMap<>();

        result.put("code", 200);
        result.put("data", userService.list()); // 你应该已有这个方法

        return result;
    }

    //  删除用户
    @DeleteMapping("/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable Integer id) {

        Map<String, Object> result = new HashMap<>();

        userService.delete(id);

        result.put("code", 200);
        result.put("msg", "删除成功");

        return result;
    }
}