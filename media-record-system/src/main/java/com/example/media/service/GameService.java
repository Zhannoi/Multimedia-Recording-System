package com.example.media.service;

import com.example.media.entity.Anime;
import com.example.media.entity.Game;
import com.example.media.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏业务层
 */
@Service
public class GameService {

    @Autowired
    private GameMapper gameMapper;

    public void add(Game game) {

        // 游戏完成状态是“已通关”
        if ("已完成".equals(game.getStatus())) {
            game.setFinishTime(new Date());
        }

        gameMapper.insert(game);
    }

    public List<Game> list(Integer userId) {
        return gameMapper.findByUserId(userId);
    }

    public Game getById(Integer id) {
        return gameMapper.findById(id);
    }

    public void update(Game game) {

        if ("已完成".equals(game.getStatus())) {
            game.setFinishTime(new Date());
        }

        gameMapper.update(game);
    }

    public void delete(Integer id) {
        gameMapper.delete(id);
    }

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