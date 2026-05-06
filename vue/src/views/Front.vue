<template>
  <div class="front-layout">
    <div class="front-header">
      <div class="header-inner">
        <div class="header-left">
          <img src="@/assets/imgs/logo.png" alt="" class="logo">
          <span class="site-name">图书管理系统</span>
        </div>
        <div class="header-center">
          <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal" :ellipsis="false">
            <el-menu-item index="/front/home">首页</el-menu-item>
            <el-menu-item index="/front/books">图书浏览</el-menu-item>
            <el-menu-item v-if="data.user.id" index="/front/myBorrow">我的借阅</el-menu-item>
          </el-menu>
        </div>
        <div class="header-right">
          <div v-if="!data.user.id">
            <el-button size="small" @click="router.push('/login')">登录</el-button>
            <el-button size="small" type="primary" @click="router.push('/register')">注册</el-button>
          </div>
          <div v-else>
            <el-dropdown style="cursor: pointer">
              <div style="display: flex; align-items: center; height: 60px">
                <img style="width: 32px; height: 32px; border-radius: 50%; object-fit: cover;" :src="data.user.avatar || '/favicon.ico'" alt="">
                <span style="margin: 0 5px; color: #333; font-size: 14px">{{ data.user.name }}</span>
                <el-icon><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/front/person')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/front/password')">修改密码</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/manager/home')" v-if="data.user.role === 'ADMIN' || data.user.role === 'LIBRARIAN'">后台管理</el-dropdown-item>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
    <div class="front-footer">
      <p>&copy; 2026 图书管理系统 - 让阅读更简单</p>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}
</script>

<style scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f6fa;
}
.front-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 80, 150, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}
.header-left {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}
.header-left .logo {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}
.header-left .site-name {
  font-size: 18px;
  font-weight: 700;
  color: #1a5a8a;
  margin-left: 10px;
  white-space: nowrap;
}
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  min-width: 0;
}
.header-center :deep(.el-menu--horizontal) {
  border-bottom: none !important;
  width: auto !important;
}
.header-center :deep(.el-menu-item) {
  font-size: 14px;
  height: 60px;
  line-height: 60px;
  border-bottom: 2px solid transparent;
  padding: 0 16px;
}
.header-center :deep(.el-menu-item.is-active) {
  color: #2a6a92 !important;
  border-bottom-color: #2a6a92 !important;
}
.header-center :deep(.el-menu-item:not(.is-active):hover) {
  color: #4a90b8 !important;
}
.header-center :deep(.el-sub-menu) {
  display: none !important;
}
.header-right {
  flex-shrink: 0;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.main-body {
  flex: 1;
  max-width: 1200px;
  margin: 20px auto;
  width: 100%;
  padding: 0 20px;
}
.front-footer {
  text-align: center;
  padding: 20px;
  color: #8ab4d0;
  font-size: 13px;
  background: #fff;
  border-top: 1px solid #e8f0f5;
}
</style>
