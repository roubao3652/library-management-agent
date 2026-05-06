<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1>欢迎来到图书管理系统</h1>
        <p>探索知识的海洋，尽享阅读的乐趣</p>
        <div class="search-box">
          <el-input size="large" v-model="data.keyword" placeholder="输入书名、作者或ISBN搜索图书..." @keyup.enter="search" clearable>
            <template #append>
              <el-button type="primary" @click="search" :icon="Search">搜索</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h2>图书分类</h2>
      </div>
      <div class="categories-grid">
        <el-card v-for="item in data.categories" :key="item.id" class="category-card" shadow="hover" @click="browseByCategory(item.id)">
          <div class="category-icon">
            <el-icon :size="28" color="#4a90b8"><FolderOpened /></el-icon>
          </div>
          <div class="category-name">{{ item.name }}</div>
        </el-card>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h2>最新图书</h2>
        <router-link to="/front/books" class="more-link">查看更多 &gt;</router-link>
      </div>
      <div class="books-grid">
        <el-card v-for="item in data.books" :key="item.id" class="book-card" shadow="hover" @click="viewBook(item.id)">
          <div class="book-cover">
            <el-image v-if="item.coverImage" :src="item.coverImage" style="width: 100%; height: 180px;" fit="cover" />
            <div v-else class="book-cover-placeholder">
              <el-icon :size="40" color="#b8d4e8"><Reading /></el-icon>
            </div>
          </div>
          <div class="book-info">
            <div class="book-title">{{ item.title }}</div>
            <div class="book-author">{{ item.author }}</div>
          </div>
        </el-card>
      </div>
      <div v-if="!data.books.length" class="empty-state">暂无图书数据</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { Search, FolderOpened, Reading } from "@element-plus/icons-vue";
import router from "@/router/index.js";

const data = reactive({
  keyword: '',
  categories: [],
  books: []
})

const search = () => {
  router.push({ path: '/front/books', query: { keyword: data.keyword } })
}

const browseByCategory = (id) => {
  router.push({ path: '/front/books', query: { categoryId: id } })
}

const viewBook = (id) => {
  router.push({ path: '/front/bookDetail', query: { id } })
}

onMounted(() => {
  request.get('/bookCategory/selectAll').then(res => {
    if (res.code === '200') data.categories = (res.data || []).slice(0, 8)
  })
  request.get('/book/selectPage', { params: { pageNum: 1, pageSize: 8 } }).then(res => {
    if (res.code === '200') data.books = res.data?.list || []
  })
})
</script>

<style scoped>
.home-page { min-height: 500px; }
.hero-section {
  background: linear-gradient(135deg, #e8f4f8 0%, #b8d4e8 50%, #7fb8d8 100%);
  border-radius: 12px;
  padding: 60px 40px;
  text-align: center;
  margin-bottom: 30px;
}
.hero-content h1 { font-size: 32px; color: #1a5a8a; margin: 0 0 10px; }
.hero-content p { color: #4a7a9a; margin: 0 0 25px; font-size: 16px; }
.search-box { max-width: 500px; margin: 0 auto; }
.search-box :deep(.el-input-group__append) { background: #4a90b8; border-color: #4a90b8; }
.search-box :deep(.el-input-group__append .el-button) { color: #fff; }

.section { margin-bottom: 30px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.section-header h2 { font-size: 20px; color: #1a5a8a; margin: 0; }
.more-link { color: #4a90b8; text-decoration: none; font-size: 14px; }

.categories-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }
.category-card { cursor: pointer; text-align: center; border-radius: 8px; }
.category-card:hover { transform: translateY(-2px); }
.category-icon { margin: 10px 0; }
.category-name { font-size: 14px; color: #333; }

.books-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; }
.book-card { cursor: pointer; border-radius: 8px; }
.book-card:hover { transform: translateY(-3px); }
.book-cover { height: 180px; display: flex; align-items: center; justify-content: center; background: #f5f9fc; border-radius: 4px; overflow: hidden; }
.book-cover-placeholder { display: flex; align-items: center; justify-content: center; width: 100%; height: 100%; }
.book-info { padding: 8px 0; }
.book-title { font-size: 14px; font-weight: 600; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.book-author { font-size: 12px; color: #8ab4d0; margin-top: 4px; }

.empty-state { text-align: center; padding: 40px; color: #8ab4d0; }
</style>
