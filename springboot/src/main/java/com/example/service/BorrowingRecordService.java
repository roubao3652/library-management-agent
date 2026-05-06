package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.BorrowingRecord;
import com.example.entity.User;
import com.example.mapper.BorrowingRecordMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowingRecordService {

    @Resource
    private BorrowingRecordMapper borrowingRecordMapper;

    public void add(BorrowingRecord record) {
        borrowingRecordMapper.insert(record);
    }

    public void updateById(BorrowingRecord record) {
        borrowingRecordMapper.updateById(record);
    }

    public void deleteById(Integer id) {
        borrowingRecordMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            borrowingRecordMapper.deleteById(id);
        }
    }

    public BorrowingRecord selectById(Integer id) {
        return borrowingRecordMapper.selectById(id);
    }

    public List<BorrowingRecord> selectAll(BorrowingRecord record) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.READER.name().equals(currentUser.getRole())) {
            record.setReaderName(((User) currentUser).getName());
        }
        return borrowingRecordMapper.selectAll(record);
    }

    public PageInfo<BorrowingRecord> selectPage(BorrowingRecord record, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.READER.name().equals(currentUser.getRole())) {
            record.setReaderName(((User) currentUser).getName());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BorrowingRecord> list = borrowingRecordMapper.selectAll(record);
        return PageInfo.of(list);
    }
}
