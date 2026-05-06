package com.example.mapper;

import com.example.entity.Book;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface BookMapper {
    int insert(Book book);
    void updateById(Book book);
    void deleteById(Integer id);
    @Select("select b.*, c.name as categoryName from books b left join book_categories c on b.category_id = c.id where b.id = #{id}")
    Book selectById(Integer id);
    List<Book> selectAll(Book book);
    @Select("select count(*) from books")
    long selectCount();
    @Select("select c.name as name, count(b.id) as value from book_categories c left join books b on c.id = b.category_id group by c.id, c.name")
    List<Map<String, Object>> selectCountByCategory();
}
