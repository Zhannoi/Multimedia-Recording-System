package com.example.media.service;

import com.example.media.entity.Anime;
import com.example.media.mapper.AnimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 动漫业务层
 */
@Service
public class AnimeService {

    @Autowired
    private AnimeMapper animeMapper;

    public void add(Anime anime) {

        if ("已完成".equals(anime.getStatus())) {
            anime.setFinishTime(new Date());
        }

        animeMapper.insert(anime);
    }

    public List<Anime> list(Integer userId) {
        return animeMapper.findByUserId(userId);
    }

    public Anime getById(Integer id) {
        return animeMapper.findById(id);
    }

    public void update(Anime anime) {

        if ("已完成".equals(anime.getStatus())) {
            anime.setFinishTime(new Date());
        }

        animeMapper.update(anime);
    }

    public void delete(Integer id) {
        animeMapper.delete(id);
    }
}