<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <div class="stat-card" style="background: linear-gradient(135deg, #4a90b8, #2a6a92);">
        <div class="stat-icon"><el-icon :size="32"><Reading /></el-icon></div>
        <div class="stat-info">
          <div class="stat-num">{{ data.stats.bookCount }}</div>
          <div class="stat-label">图书总量</div>
        </div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #67c23a, #529b2e);">
        <div class="stat-icon"><el-icon :size="32"><FolderOpened /></el-icon></div>
        <div class="stat-info">
          <div class="stat-num">{{ data.stats.categoryCount }}</div>
          <div class="stat-label">分类数量</div>
        </div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #e6a23c, #b88230);">
        <div class="stat-icon"><el-icon :size="32"><Tickets /></el-icon></div>
        <div class="stat-info">
          <div class="stat-num">{{ data.stats.totalBorrows }}</div>
          <div class="stat-label">借阅总次</div>
        </div>
      </div>
      <div class="stat-card" style="background: linear-gradient(135deg, #f56c6c, #c45656);">
        <div class="stat-icon"><el-icon :size="32"><WarningFilled /></el-icon></div>
        <div class="stat-info">
          <div class="stat-num">{{ data.stats.pendingBorrows }}</div>
          <div class="stat-label">待审核</div>
        </div>
      </div>
    </div>

    <!-- 图表区 -->
    <div class="chart-row">
      <div class="chart-box">
        <div class="chart-title">图书分类分布</div>
        <div ref="categoryChart" style="height: 300px;"></div>
      </div>
      <div class="chart-box">
        <div class="chart-title">借阅状态统计</div>
        <div ref="statusChart" style="height: 300px;"></div>
      </div>
    </div>

    <!-- 最近借阅 -->
    <div class="card" style="margin-top: 15px;">
      <div style="font-size: 16px; font-weight: 600; color: #1a5a8a; margin-bottom: 10px;">最近借阅记录</div>
      <el-table :data="data.recentBorrows" stripe size="small" v-if="data.recentBorrows.length">
        <el-table-column prop="bookTitle" label="书名" show-overflow-tooltip />
        <el-table-column prop="bookAuthor" label="作者" width="150" />
        <el-table-column prop="readerName" label="借阅人" width="100" />
        <el-table-column prop="borrowDate" label="借阅日期" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template v-slot="scope">
            <el-tag :type="scope.row.status === 'RETURNED' ? 'info' : scope.row.status === 'APPROVED' ? 'success' : scope.row.status === 'REJECTED' ? 'danger' : 'warning'" size="small">
              {{ {PENDING:'待审核',APPROVED:'已借出',RETURNED:'已归还',REJECTED:'已拒绝'}[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted, nextTick} from "vue";
import request from "@/utils/request.js";
import * as echarts from 'echarts';
import {Reading, FolderOpened, Tickets, WarningFilled} from "@element-plus/icons-vue";

const data = reactive({
  stats: { bookCount: 0, categoryCount: 0, totalBorrows: 0, pendingBorrows: 0 },
  recentBorrows: []
})

const categoryChart = ref(null)
const statusChart = ref(null)

let categoryChartInstance = null
let statusChartInstance = null

const loadStats = () => {
  request.get('/dashboard/stats').then(res => {
    if (res.code === '200') data.stats = res.data || {}
  })
}

const loadCategoryChart = () => {
  request.get('/dashboard/booksByCategory').then(res => {
    if (res.code === '200' && res.data?.length) {
      nextTick(() => {
        if (categoryChart.value) {
          categoryChartInstance = echarts.init(categoryChart.value)
          categoryChartInstance.setOption({
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { fontSize: 12 } },
            series: [{
              type: 'pie',
              radius: ['40%', '65%'],
              center: ['50%', '40%'],
              avoidLabelOverlap: true,
              itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
              label: { show: true, formatter: '{b}: {c}' },
              data: res.data
            }]
          })
        }
      })
    }
  })
}

const loadStatusChart = () => {
  nextTick(() => {
    if (statusChart.value) {
      statusChartInstance = echarts.init(statusChart.value)
      statusChartInstance.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { fontSize: 12 } },
        color: ['#e6a23c', '#67c23a', '#909399', '#f56c6c'],
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['50%', '40%'],
          label: { show: true, formatter: '{b}: {c}' },
          data: [
            { name: '待审核', value: data.stats.pendingBorrows || 0 },
            { name: '已借出', value: data.stats.approvedBorrows || 0 },
            { name: '已归还', value: data.stats.returnedBorrows || 0 }
          ]
        }]
      })
    }
  })
}

const loadRecent = () => {
  request.get('/dashboard/recentBorrows').then(res => {
    if (res.code === '200') data.recentBorrows = res.data || []
  })
}

onMounted(() => {
  loadStats()
  loadCategoryChart()
  loadRecent()
})

// 监听 stats 变化后渲染借阅状态图表
import {watch} from "vue";
watch(() => data.stats, () => { nextTick(loadStatusChart) }, { deep: true })
</script>

<style scoped>
.dashboard { min-height: 500px; }
.stat-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 15px; }
.stat-card {
  border-radius: 10px; padding: 20px; display: flex; align-items: center; gap: 15px; color: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.stat-icon { opacity: 0.8; }
.stat-num { font-size: 28px; font-weight: 700; line-height: 1.2; }
.stat-label { font-size: 13px; opacity: 0.85; margin-top: 2px; }
.chart-row { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
.chart-box { background: #fff; border-radius: 8px; padding: 15px; box-shadow: 0 1px 4px rgba(0,80,150,0.06); }
.chart-title { font-size: 15px; font-weight: 600; color: #1a5a8a; margin-bottom: 10px; }
</style>
