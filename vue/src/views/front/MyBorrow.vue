<template>
  <div class="my-borrow-page">
    <div class="page-title">
      <h2>我的借阅</h2>
    </div>

    <div class="filter-bar">
      <el-select v-model="data.status" clearable placeholder="按状态筛选" style="width: 150px" @change="load">
        <el-option label="待审核" value="PENDING" />
        <el-option label="已借出" value="APPROVED" />
        <el-option label="已归还" value="RETURNED" />
        <el-option label="已拒绝" value="REJECTED" />
      </el-select>
    </div>

    <el-table stripe :data="data.tableData" v-if="data.tableData.length" class="borrow-table">
      <el-table-column label="封面" width="60">
        <template v-slot="scope">
          <el-image v-if="scope.row.bookCover" :src="scope.row.bookCover" style="width: 36px; height: 48px; border-radius: 3px;" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="bookTitle" label="书名" show-overflow-tooltip />
      <el-table-column prop="bookAuthor" label="作者" />
      <el-table-column prop="borrowDate" label="借阅日期" width="110" />
      <el-table-column prop="dueDate" label="应还日期" width="110" />
      <el-table-column prop="returnDate" label="归还日期" width="110" />
      <el-table-column prop="status" label="状态" width="90">
        <template v-slot="scope">
          <el-tag :type="scope.row.status === 'APPROVED' ? 'success' : scope.row.status === 'RETURNED' ? 'info' : scope.row.status === 'REJECTED' ? 'danger' : 'warning'" size="small">
            {{ scope.row.status === 'PENDING' ? '待审核' : scope.row.status === 'APPROVED' ? '已借出' : scope.row.status === 'RETURNED' ? '已归还' : '已拒绝' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="!data.tableData.length" class="empty-state">
      <el-empty description="暂无借阅记录" />
    </div>

    <div class="pagination-wrapper" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  status: null
})

const load = () => {
  request.get('/borrowingRecord/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

onMounted(load)
</script>

<style scoped>
.my-borrow-page { min-height: 500px; }
.page-title h2 { font-size: 20px; color: #1a5a8a; margin: 0 0 15px; }
.filter-bar { margin-bottom: 15px; }
.borrow-table { background: #fff; border-radius: 8px; }
.empty-state { padding: 60px 0; }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: center; }
</style>
