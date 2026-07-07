package com.example.media.controller;

import com.example.media.entity.Anime;
import com.example.media.mapper.AnimeMapper;
import com.example.media.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 动漫控制层
 */
@RestController
@RequestMapping("/api/anime")
@CrossOrigin
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @PostMapping("/add")
    public String add(@RequestBody Anime anime) {
        animeService.add(anime);
        return "添加成功";
    }

    @GetMapping("/list")
    public List<Anime> list(@RequestParam Integer userId) {
        return animeService.list(userId);
    }

    @GetMapping("/{id}")
    public Anime getById(@PathVariable Integer id) {
        return animeService.getById(id);
    }

    @PutMapping("/update")
    public String update(@RequestBody Anime anime) {
        animeService.update(anime);
        return "修改成功";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        animeService.delete(id);
        return "删除成功";
    }

    @Autowired
    private AnimeMapper animeMapper;

    /**
     * 按名字搜索动漫记录
     */
    @GetMapping("/search")
    public Map<String,Object> searchByName(
            @RequestParam Integer userId,
            @RequestParam String keyword
    ){
        List<Anime> list = animeMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");

        // 返回code和data，前端可以直接用
        Map<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", list);
        return map;
    }
}