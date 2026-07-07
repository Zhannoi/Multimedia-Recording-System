package com.example.media.mapper;

import com.example.media.entity.Anime;
import com.example.media.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 游戏记录Mapper
 */
@Mapper
public interface GameMapper {

    int insert(Game game);

    List<Game> findByUserId(@Param("userId") Integer userId);

    Game findById(@Param("id") Integer id);

    int update(Game game);

    int delete(@Param("id") Integer id);

    List<Game> findByUserIdAndNameLike(@Param("userId") Integer userId, @Param("name") String name);
}