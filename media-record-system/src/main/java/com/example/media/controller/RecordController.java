package com.example.media.controller;

import com.example.media.entity.Movie;
import com.example.media.entity.Book;
import com.example.media.entity.Game;
import com.example.media.entity.Anime;
import com.example.media.mapper.MovieMapper;
import com.example.media.mapper.BookMapper;
import com.example.media.mapper.GameMapper;
import com.example.media.mapper.AnimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private AnimeMapper animeMapper;

    /**
     * 全局按名称搜索
     */
    @GetMapping("/search")
    public Map<String, Object> searchAll(
            @RequestParam String keyword,
            @RequestParam Integer userId
    ) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> allRecords = new ArrayList<>();

        // movie
        List<Movie> movies = movieMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");
        for (Movie m : movies) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("name", m.getName());
            map.put("type", "movie");   // 关键
            map.put("status", m.getStatus());
            map.put("cover", m.getCover());
            allRecords.add(map);
        }

        // book
        List<Book> books = bookMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");
        for (Book b : books) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", b.getId());
            map.put("name", b.getName());
            map.put("type", "book");
            map.put("status", b.getStatus());
            map.put("cover", b.getCover());
            allRecords.add(map);
        }

        // game
        List<Game> games = gameMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");
        for (Game g : games) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", g.getId());
            map.put("name", g.getName());
            map.put("type", "game");
            map.put("status", g.getStatus());
            map.put("cover", g.getCover());
            allRecords.add(map);
        }

        // anime
        List<Anime> animes = animeMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");
        for (Anime a : animes) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("name", a.getName());
            map.put("type", "anime");
            map.put("status", a.getStatus());
            map.put("cover", a.getCover());
            allRecords.add(map);
        }

        result.put("code", 200);
        result.put("data", allRecords);
        return result;
    }
}