package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.BookCategory;
import com.example.mapper.BookCategoryMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryService {

    @Resource
    private BookCategoryMapper bookCategoryMapper;

    public void add(BookCategory category) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null) {
            category.setUserId(currentUser.getId());
        }
        bookCategoryMapper.insert(category);
    }

    public void updateById(BookCategory category) {
        bookCategoryMapper.updateById(category);
    }

    public void deleteById(Integer id) {
        bookCategoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            bookCategoryMapper.deleteById(id);
        }
    }

    public BookCategory selectById(Integer id) {
        return bookCategoryMapper.selectById(id);
    }

    public List<BookCategory> selectAll(BookCategory category) {
        return bookCategoryMapper.selectAll(category);
    }

    public PageInfo<BookCategory> selectPage(BookCategory category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BookCategory> list = bookCategoryMapper.selectAll(category);
        return PageInfo.of(list);
    }
}
