package com.example.media.mapper;

import com.example.media.entity.Anime;
import com.example.media.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 书籍记录Mapper
 */
@Mapper
public interface BookMapper {

    /**
     * 新增记录
     */
    int insert(Book book);

    /**
     * 查询某个用户的所有书籍记录
     */
    List<Book> findByUserId(@Param("userId") Integer userId);

    /**
     * 根据ID查询详情
     */
    Book findById(@Param("id") Integer id);

    /**
     * 更新记录
     */
    int update(Book book);

    /**
     * 删除记录
     */
    int delete(@Param("id") Integer id);

    List<Book> findByUserIdAndNameLike(@Param("userId") Integer userId, @Param("name") String name);
}