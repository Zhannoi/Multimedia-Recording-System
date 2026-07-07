package com.example.media.service;

import com.example.media.entity.User;
import com.example.media.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务层
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    public String register(User user) {

        // 1. 判断用户名是否已存在
        User existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            return "用户名已存在";
        }

        // 2. 插入数据库
        userMapper.insert(user);

        return "注册成功";
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {

        // 1. 查询用户
        User user = userMapper.findByUsername(username);

        // 2. 判断用户是否存在
        if (user == null) {
            return null;
        }

        // 3. 判断密码是否正确
        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    /**
     * 获取所有用户（管理员用）
     */
    public List<User> list() {
        return userMapper.findAll();
    }

    /**
     * 删除用户（管理员用）
     */
    public void delete(Integer id) {
        userMapper.delete(id);
    }

}