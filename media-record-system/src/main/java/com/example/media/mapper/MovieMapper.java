package com.example.media.mapper;

import com.example.media.entity.Anime;
import com.example.media.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 影视记录Mapper
 */
@Mapper
public interface MovieMapper {

    /**
     * 新增记录
     */
    int insert(Movie movie);

    /**
     * 根据用户查询所有记录
     */
    List<Movie> findByUserId(@Param("userId") Integer userId);

    /**
     * 根据ID查询详情
     */
    Movie findById(@Param("id") Integer id);

    /**
     * 更新记录
     */
    int update(Movie movie);

    /**
     * 删除记录
     */
    int delete(@Param("id") Integer id);

    List<Movie> findByUserIdAndNameLike(@Param("userId") Integer userId, @Param("name") String name);

}