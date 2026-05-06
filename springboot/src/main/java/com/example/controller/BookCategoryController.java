package com.example.controller;

import com.example.common.Result;
import com.example.entity.BookCategory;
import com.example.service.BookCategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookCategory")
public class BookCategoryController {

    @Resource
    private BookCategoryService bookCategoryService;

    @PostMapping("/add")
    public Result add(@RequestBody BookCategory category) {
        bookCategoryService.add(category);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody BookCategory category) {
        bookCategoryService.updateById(category);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        bookCategoryService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        bookCategoryService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookCategory category = bookCategoryService.selectById(id);
        return Result.success(category);
    }

    @GetMapping("/selectAll")
    public Result selectAll(BookCategory category) {
        List<BookCategory> list = bookCategoryService.selectAll(category);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(BookCategory category,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookCategory> pageInfo = bookCategoryService.selectPage(category, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
