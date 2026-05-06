<template>
  <div class="detail-page" v-if="data.book.id">
    <el-card class="detail-card" shadow="hover">
      <div class="detail-layout">
        <div class="detail-cover">
          <el-image v-if="data.book.coverImage" :src="data.book.coverImage" style="width: 260px; height: 350px; border-radius: 8px;" fit="cover" />
          <div v-else class="cover-placeholder">
            <el-icon :size="60" color="#b8d4e8"><Reading /></el-icon>
            <span>暂无封面</span>
          </div>
        </div>
        <div class="detail-info">
          <h1 class="book-title">{{ data.book.title }}</h1>
          <div class="info-row"><span class="label">作者：</span>{{ data.book.author || '未知' }}</div>
          <div class="info-row"><span class="label">出版社：</span>{{ data.book.publisher || '未知' }}</div>
          <div class="info-row"><span class="label">ISBN：</span>{{ data.book.isbn || '未知' }}</div>
          <div class="info-row"><span class="label">分类：</span><span class="tag">{{ data.book.categoryName || '未分类' }}</span></div>
          <div class="info-row"><span class="label">出版日期：</span>{{ data.book.publishDate || '未知' }}</div>
          <div class="info-row">
            <span class="label">库存：</span>
            <span :class="data.book.stock > 0 ? 'stock-ok' : 'stock-none'">
              {{ data.book.stock > 0 ? data.book.stock + ' 册' : '已借完' }}
            </span>
          </div>
          <div class="info-desc" v-if="data.book.description">
            <span class="label">简介：</span>
            <p>{{ data.book.description }}</p>
          </div>
          <div class="detail-actions" v-if="data.user.id">
            <el-button type="primary" size="large" :disabled="data.book.stock <= 0" @click="borrowBook">
              申请借阅
            </el-button>
          </div>
          <div class="detail-actions" v-else>
            <el-button type="primary" size="large" @click="router.push('/login')">登录后借阅</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-dialog title="申请借阅" v-model="data.borrowVisible" width="400px">
      <el-form :model="data.borrowForm" label-width="80px">
        <el-form-item label="借阅人">
          <el-input v-model="data.borrowForm.readerName" placeholder="请输入借阅人姓名" />
        </el-form-item>
        <el-form-item label="借阅天数">
          <el-select v-model="data.borrowForm.days" style="width: 100%">
            <el-option label="7天" :value="7" />
            <el-option label="14天" :value="14" />
            <el-option label="30天" :value="30" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.borrowVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBorrow">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
  <div v-else class="loading-state">
    <el-skeleton :rows="5" animated />
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { Reading } from "@element-plus/icons-vue";
import router from "@/router/index.js";
import { useRoute } from "vue-router";

const route = useRoute()
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

const data = reactive({
  book: {},
  user: user,
  borrowVisible: false,
  borrowForm: { readerName: user.name || '', days: 14 }
})

const load = () => {
  const id = route.query.id
  if (!id) return
  request.get('/book/selectById/' + id).then(res => {
    if (res.code === '200') data.book = res.data || {}
  })
}

const borrowBook = () => {
  data.borrowForm = { readerName: data.user.name || '', days: 14 }
  data.borrowVisible = true
}

const submitBorrow = () => {
  if (!data.borrowForm.readerName) {
    ElMessage.warning('请输入借阅人姓名')
    return
  }
  const now = new Date()
  const borrowDate = now.getFullYear() + '-' + String(now.getMonth()+1).padStart(2,'0') + '-' + String(now.getDate()).padStart(2,'0')
  const due = new Date(now.getTime() + data.borrowForm.days * 24 * 60 * 60 * 1000)
  const dueDate = due.getFullYear() + '-' + String(due.getMonth()+1).padStart(2,'0') + '-' + String(due.getDate()).padStart(2,'0')

  request.post('/borrowingRecord/add', {
    userId: user.id,
    bookId: data.book.id,
    readerName: data.borrowForm.readerName,
    borrowDate: borrowDate,
    dueDate: dueDate,
    status: 'PENDING'
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('借阅申请已提交，请等待审核')
      data.borrowVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(load)
</script>

<style scoped>
.detail-page { min-height: 500px; }
.detail-card { border-radius: 12px; }
.detail-layout { display: flex; gap: 30px; padding: 10px; }
.detail-cover { flex-shrink: 0; }
.cover-placeholder { width: 260px; height: 350px; border-radius: 8px; background: #f5f9fc; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 10px; color: #8ab4d0; font-size: 13px; }
.detail-info { flex: 1; }
.book-title { font-size: 24px; color: #1a5a8a; margin: 0 0 20px; }
.info-row { margin-bottom: 12px; font-size: 14px; color: #555; }
.info-row .label { color: #8ab4d0; font-weight: 500; }
.tag { font-size: 12px; background: #e8f4f8; color: #4a90b8; padding: 2px 8px; border-radius: 4px; }
.stock-ok { color: #67c23a; font-weight: 600; }
.stock-none { color: #f56c6c; font-weight: 600; }
.info-desc { margin-top: 15px; }
.info-desc p { color: #666; line-height: 1.8; font-size: 14px; margin: 8px 0; }
.detail-actions { margin-top: 25px; }
.loading-state { padding: 40px; }
</style>
