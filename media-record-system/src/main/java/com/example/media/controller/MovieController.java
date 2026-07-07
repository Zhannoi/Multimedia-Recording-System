package com.example.media.controller;

import com.example.media.entity.Anime;
import com.example.media.entity.Movie;
import com.example.media.mapper.MovieMapper;
import com.example.media.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 影视控制层
 */
@RestController
@RequestMapping("/api/movie")
@CrossOrigin
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * 添加记录
     */
    @PostMapping("/add")
    public String add(@RequestBody Movie movie) {
        movieService.add(movie);
        return "添加成功";
    }

    /**
     * 查询列表（根据用户ID）
     */
    @GetMapping("/list")
    public List<Movie> list(@RequestParam Integer userId) {
        return movieService.list(userId);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    public Movie getById(@PathVariable Integer id) {
        return movieService.getById(id);
    }

    /**
     * 修改记录
     */
    @PutMapping("/update")
    public String update(@RequestBody Movie movie) {
        movieService.update(movie);
        return "修改成功";
    }

    /**
     * 删除记录
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        movieService.delete(id);
        return "删除成功";
    }

    @Autowired
    private MovieMapper movieMapper;

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