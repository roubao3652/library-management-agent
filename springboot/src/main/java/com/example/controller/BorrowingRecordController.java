package com.example.controller;

import com.example.common.Result;
import com.example.entity.BorrowingRecord;
import com.example.service.BorrowingRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrowingRecord")
public class BorrowingRecordController {

    @Resource
    private BorrowingRecordService borrowingRecordService;

    @PostMapping("/add")
    public Result add(@RequestBody BorrowingRecord record) {
        borrowingRecordService.add(record);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody BorrowingRecord record) {
        borrowingRecordService.updateById(record);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        borrowingRecordService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        borrowingRecordService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BorrowingRecord record = borrowingRecordService.selectById(id);
        return Result.success(record);
    }

    @GetMapping("/selectAll")
    public Result selectAll(BorrowingRecord record) {
        List<BorrowingRecord> list = borrowingRecordService.selectAll(record);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(BorrowingRecord record,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BorrowingRecord> pageInfo = borrowingRecordService.selectPage(record, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
