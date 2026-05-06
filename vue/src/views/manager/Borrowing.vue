<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-select v-model="data.status" clearable style="width: 180px; margin-right: 10px" placeholder="按状态查询">
        <el-option label="待审核" value="PENDING" />
        <el-option label="已借出" value="APPROVED" />
        <el-option label="已归还" value="RETURNED" />
        <el-option label="已拒绝" value="REJECTED" />
      </el-select>
      <el-input v-model="data.readerName" prefix-icon="Search" style="width: 200px; margin-right: 10px" placeholder="按借阅人查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="bookTitle" label="书名" show-overflow-tooltip />
        <el-table-column prop="bookAuthor" label="作者" />
        <el-table-column prop="readerName" label="借阅人" />
        <el-table-column prop="borrowDate" label="借阅日期" width="110" />
        <el-table-column prop="dueDate" label="应还日期" width="110" />
        <el-table-column prop="returnDate" label="归还日期" width="110" />
        <el-table-column prop="status" label="状态" width="90">
          <template v-slot="scope">
            <el-tag :type="scope.row.status === 'APPROVED' ? 'success' : scope.row.status === 'RETURNED' ? 'info' : scope.row.status === 'REJECTED' ? 'danger' : 'warning'">
              {{ scope.row.status === 'PENDING' ? '待审核' : scope.row.status === 'APPROVED' ? '已借出' : scope.row.status === 'RETURNED' ? '已归还' : '已拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template v-slot="scope">
            <el-button type="success" size="small" plain v-if="scope.row.status === 'PENDING'" @click="approve(scope.row)">通过</el-button>
            <el-button type="danger" size="small" plain v-if="scope.row.status === 'PENDING'" @click="reject(scope.row)">拒绝</el-button>
            <el-button type="primary" size="small" plain v-if="scope.row.status === 'APPROVED'" @click="returnBook(scope.row)">归还</el-button>
            <el-button type="danger" circle :icon="Delete" size="small" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete} from "@element-plus/icons-vue";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  status: null,
  readerName: null,
  ids: []
})

const load = () => {
  request.get('/borrowingRecord/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      status: data.status,
      readerName: data.readerName
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const approve = (row) => {
  row.status = 'APPROVED'
  request.put('/borrowingRecord/update', row).then(res => {
    if (res.code === '200') { ElMessage.success('已通过'); load() }
  })
}

const reject = (row) => {
  row.status = 'REJECTED'
  request.put('/borrowingRecord/update', row).then(res => {
    if (res.code === '200') { ElMessage.success('已拒绝'); load() }
  })
}

const returnBook = (row) => {
  const now = new Date()
  const dateStr = now.getFullYear() + '-' + String(now.getMonth()+1).padStart(2,'0') + '-' + String(now.getDate()).padStart(2,'0')
  row.returnDate = dateStr
  row.status = 'RETURNED'
  request.put('/borrowingRecord/update', row).then(res => {
    if (res.code === '200') { ElMessage.success('归还成功'); load() }
  })
}

const del = (id) => {
  ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' }).then(res => {
    request.delete('/borrowingRecord/delete/' + id).then(res => {
      if (res.code === '200') { ElMessage.success("删除成功"); load() }
    })
  }).catch(err => {})
}

const delBatch = () => {
  if (!data.ids.length) { ElMessage.warning("请选择数据"); return }
  ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' }).then(res => {
    request.delete("/borrowingRecord/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') { ElMessage.success('操作成功'); load() }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.status = null
  data.readerName = null
  load()
}

load()
</script>
