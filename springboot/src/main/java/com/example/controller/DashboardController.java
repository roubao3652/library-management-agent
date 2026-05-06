package com.example.controller;

import com.example.common.Result;
import com.example.mapper.BookCategoryMapper;
import com.example.mapper.BookMapper;
import com.example.mapper.BorrowingRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BookCategoryMapper bookCategoryMapper;

    @Resource
    private BorrowingRecordMapper borrowingRecordMapper;

    @GetMapping("/stats")
    public Result stats() {
        long bookCount = bookMapper.selectCount();
        long categoryCount = bookCategoryMapper.selectCount();
        Map<String, Object> borrowStats = borrowingRecordMapper.selectStatusCount();

        Map<String, Object> data = new HashMap<>();
        data.put("bookCount", bookCount);
        data.put("categoryCount", categoryCount);
        data.put("totalBorrows", borrowStats.get("total"));
        data.put("pendingBorrows", borrowStats.get("PENDING"));
        data.put("approvedBorrows", borrowStats.get("APPROVED"));
        data.put("returnedBorrows", borrowStats.get("RETURNED"));

        return Result.success(data);
    }

    @GetMapping("/booksByCategory")
    public Result booksByCategory() {
        List<Map<String, Object>> list = bookMapper.selectCountByCategory();
        return Result.success(list);
    }

    @GetMapping("/recentBorrows")
    public Result recentBorrows() {
        List<Map<String, Object>> list = borrowingRecordMapper.selectRecent();
        return Result.success(list);
    }
}
