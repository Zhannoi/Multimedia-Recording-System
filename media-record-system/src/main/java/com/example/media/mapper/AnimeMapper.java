package com.example.media.mapper;

import com.example.media.entity.Anime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动漫记录Mapper
 */
@Mapper
public interface AnimeMapper {

    int insert(Anime anime);

    List<Anime> findByUserId(@Param("userId") Integer userId);

    Anime findById(@Param("id") Integer id);

    int update(Anime anime);

    int delete(@Param("id") Integer id);

    List<Anime> findByUserIdAndNameLike(@Param("userId") Integer userId, @Param("name") String name);
}