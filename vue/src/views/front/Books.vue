<template>
  <div class="books-page">
    <div class="filter-bar">
      <el-input v-model="data.keyword" prefix-icon="Search" placeholder="按书名/作者搜索" style="width: 260px" clearable @clear="load" @keyup.enter="load" />
      <el-select v-model="data.categoryId" clearable placeholder="按分类筛选" style="width: 160px" @change="load">
        <el-option v-for="item in data.categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button type="primary" @click="load">搜索</el-button>
    </div>

    <div class="books-grid">
      <el-card v-for="item in data.tableData" :key="item.id" class="book-card" shadow="hover" @click="viewBook(item.id)">
        <div class="book-cover">
          <el-image v-if="item.coverImage" :src="item.coverImage" style="width: 100%; height: 200px;" fit="cover" />
          <div v-else class="book-cover-placeholder">
            <el-icon :size="50" color="#b8d4e8"><Reading /></el-icon>
          </div>
        </div>
        <div class="book-info">
          <div class="book-title">{{ item.title }}</div>
          <div class="book-author">{{ item.author || '未知作者' }}</div>
          <div class="book-meta">
            <span class="tag">{{ item.categoryName || '未分类' }}</span>
            <span :class="item.stock > 0 ? 'stock-ok' : 'stock-none'">{{ item.stock > 0 ? '在库' : '已借完' }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <div v-if="!data.tableData.length" class="empty-state">
      <el-empty description="暂无图书" />
    </div>

    <div class="pagination-wrapper" v-if="data.total > data.pageSize">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { Reading } from "@element-plus/icons-vue";
import router from "@/router/index.js";
import { useRoute } from "vue-router";

const route = useRoute()

const data = reactive({
  tableData: [],
  categories: [],
  pageNum: 1,
  pageSize: 12,
  total: 0,
  keyword: route.query.keyword || '',
  categoryId: route.query.categoryId || null
})

const load = () => {
  request.get('/book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.keyword,
      author: data.keyword,
      categoryId: data.categoryId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const viewBook = (id) => {
  router.push({ path: '/front/bookDetail', query: { id } })
}

onMounted(() => {
  request.get('/bookCategory/selectAll').then(res => {
    if (res.code === '200') data.categories = res.data || []
  })
  load()
})
</script>

<style scoped>
.books-page { min-height: 500px; }
.filter-bar {
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.books-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; }
.book-card { cursor: pointer; border-radius: 8px; }
.book-card:hover { transform: translateY(-3px); }
.book-cover { height: 200px; display: flex; align-items: center; justify-content: center; background: #f5f9fc; border-radius: 4px; overflow: hidden; }
.book-cover-placeholder { display: flex; align-items: center; justify-content: center; width: 100%; height: 100%; }
.book-info { padding: 10px 0; }
.book-title { font-size: 15px; font-weight: 600; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.book-author { font-size: 13px; color: #8ab4d0; margin-top: 4px; }
.book-meta { display: flex; justify-content: space-between; margin-top: 8px; align-items: center; }
.tag { font-size: 12px; background: #e8f4f8; color: #4a90b8; padding: 2px 8px; border-radius: 4px; }
.stock-ok { font-size: 12px; color: #67c23a; }
.stock-none { font-size: 12px; color: #f56c6c; }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: center; }
.empty-state { padding: 40px; }
</style>
