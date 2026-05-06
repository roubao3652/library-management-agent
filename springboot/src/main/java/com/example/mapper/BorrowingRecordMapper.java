package com.example.mapper;

import com.example.entity.BorrowingRecord;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface BorrowingRecordMapper {
    int insert(BorrowingRecord record);
    void updateById(BorrowingRecord record);
    void deleteById(Integer id);
    @Select("select * from borrowing_records where id = #{id}")
    BorrowingRecord selectById(Integer id);
    List<BorrowingRecord> selectAll(BorrowingRecord record);
    @Select("select count(*) as total, sum(case when status = 'PENDING' then 1 else 0 end) as PENDING, sum(case when status = 'APPROVED' then 1 else 0 end) as APPROVED, sum(case when status = 'RETURNED' then 1 else 0 end) as RETURNED, sum(case when status = 'REJECTED' then 1 else 0 end) as REJECTED from borrowing_records")
    Map<String, Object> selectStatusCount();
    @Select("select r.*, b.title as bookTitle, b.author as bookAuthor from borrowing_records r left join books b on r.book_id = b.id order by r.id desc limit 8")
    List<Map<String, Object>> selectRecent();
}
