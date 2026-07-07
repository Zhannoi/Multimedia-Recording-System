package com.example.media.controller;

import com.example.media.entity.Anime;
import com.example.media.entity.Book;
import com.example.media.mapper.BookMapper;
import com.example.media.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 书籍控制层
 */
@RestController
@RequestMapping("/api/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 添加书籍记录
     */
    @PostMapping("/add")
    public String add(@RequestBody Book book) {
        bookService.add(book);
        return "添加成功";
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public List<Book> list(@RequestParam Integer userId) {
        return bookService.list(userId);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    public Book getById(@PathVariable Integer id) {
        return bookService.getById(id);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public String update(@RequestBody Book book) {
        bookService.update(book);
        return "修改成功";
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        bookService.delete(id);
        return "删除成功";
    }

    @Autowired
    private BookMapper bookMapper;

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