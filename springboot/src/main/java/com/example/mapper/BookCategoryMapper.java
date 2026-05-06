package com.example.mapper;

import com.example.entity.BookCategory;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface BookCategoryMapper {
    int insert(BookCategory bookCategory);
    void updateById(BookCategory bookCategory);
    void deleteById(Integer id);
    @Select("select * from book_categories where id = #{id}")
    BookCategory selectById(Integer id);
    List<BookCategory> selectAll(BookCategory bookCategory);
    @Select("select count(*) from book_categories")
    long selectCount();
}
