package com.example.media.controller;

import com.example.media.entity.Anime;
import com.example.media.entity.Game;
import com.example.media.mapper.GameMapper;
import com.example.media.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏控制层
 */
@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/add")
    public String add(@RequestBody Game game) {
        gameService.add(game);
        return "添加成功";
    }

    @GetMapping("/list")
    public List<Game> list(@RequestParam Integer userId) {
        return gameService.list(userId);
    }

    @GetMapping("/{id}")
    public Game getById(@PathVariable Integer id) {
        return gameService.getById(id);
    }

    @PutMapping("/update")
    public String update(@RequestBody Game game) {
        gameService.update(game);
        return "修改成功";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        gameService.delete(id);
        return "删除成功";
    }

    @Autowired
    private GameMapper gameMapper;

    /**
     * 按名字搜索记录
     */
    @GetMapping("/search")
    public Map<String,Object> searchByName(
            @RequestParam Integer userId,
            @RequestParam String keyword
    ){
        List<Game> list = gameMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");

        // 返回code和data，前端可以直接用
        Map<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", list);
        return map;
    }

}