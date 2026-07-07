package com.example.media.service;

import com.example.media.entity.Anime;
import com.example.media.entity.Movie;
import com.example.media.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 影视业务层
 */
@Service
public class MovieService {

    @Autowired
    private MovieMapper movieMapper;

    /**
     * 新增记录
     */
    public void add(Movie movie) {

        // 👉 如果状态是“已看完”，自动记录完成时间
        if ("已完成".equals(movie.getStatus())) {
            movie.setFinishTime(new Date());
        }

        movieMapper.insert(movie);
    }

    /**
     * 查询用户所有记录
     */
    public List<Movie> list(Integer userId) {
        return movieMapper.findByUserId(userId);
    }

    /**
     * 查询详情
     */
    public Movie getById(Integer id) {
        return movieMapper.findById(id);
    }

    /**
     * 更新记录
     */
    public void update(Movie movie) {

        // 👉 更新状态时判断是否完成
        if ("已完成".equals(movie.getStatus())) {
            movie.setFinishTime(new Date());
        }

        movieMapper.update(movie);
    }

    /**
     * 删除记录
     */
    public void delete(Integer id) {
        movieMapper.delete(id);
    }

    /**
     * 按名字搜索记录
     */
    @GetMapping("/search")
    public Map<String,Object> searchByName(
            @RequestParam Integer userId,
            @RequestParam String keyword
    ){
        List<Movie> list = movieMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");

        // 返回code和data，前端可以直接用
        Map<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", list);
        return map;
    }
}