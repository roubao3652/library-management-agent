package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Book;
import com.example.mapper.BookMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;

    public void add(Book book) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null) {
            book.setUserId(currentUser.getId());
        }
        bookMapper.insert(book);
    }

    public void updateById(Book book) {
        bookMapper.updateById(book);
    }

    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            bookMapper.deleteById(id);
        }
    }

    public Book selectById(Integer id) {
        return bookMapper.selectById(id);
    }

    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    public PageInfo<Book> selectPage(Book book, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll(book);
        return PageInfo.of(list);
    }
}
