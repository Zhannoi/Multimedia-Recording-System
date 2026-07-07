package com.example.media.service;

import com.example.media.entity.Anime;
import com.example.media.entity.Book;
import com.example.media.mapper.AnimeMapper;
import com.example.media.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 书籍业务层
 */
@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    /**
     * 新增书籍记录
     */
    public void add(Book book) {

        // 如果状态是“已看完”，记录完成时间
        if ("已完成".equals(book.getStatus())) {
            book.setFinishTime(new Date());
        }

        bookMapper.insert(book);
    }

    /**
     * 查询用户书籍列表
     */
    public List<Book> list(Integer userId) {
        return bookMapper.findByUserId(userId);
    }

    /**
     * 查询详情
     */
    public Book getById(Integer id) {
        return bookMapper.findById(id);
    }

    /**
     * 更新
     */
    public void update(Book book) {

        if ("已完成".equals(book.getStatus())) {
            book.setFinishTime(new Date());
        }

        bookMapper.update(book);
    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        bookMapper.delete(id);
    }


    /**
     * 按名字搜索记录
     */
    @GetMapping("/search")
    public Map<String,Object> searchByName(
            @RequestParam Integer userId,
            @RequestParam String keyword
    ){
        List<Book> list = bookMapper.findByUserIdAndNameLike(userId, "%" + keyword + "%");

        // 返回code和data，前端可以直接用
        Map<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", list);
        return map;
    }
}