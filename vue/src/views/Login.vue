<template>
  <div class="login-container">
    <div class="login-bg"></div>
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">
          <img src="@/assets/imgs/logo.png" alt="">
        </div>
        <div class="login-title">图书管理系统</div>
        <div class="login-subtitle">LIBRARY MANAGEMENT SYSTEM</div>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" class="login-form">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role" placeholder="请选择角色">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="LIBRARIAN" label="普通馆员"></el-option>
            <el-option value="READER" label="读者"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="login-btn" @click="login">登 录</el-button>
        </el-form-item>
        <div class="login-footer">
          还没有账号？请 <a href="/register">立即注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: { role: 'ADMIN' },
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    role: [{ required: true, message: '请选择角色', trigger: 'change' }]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          if (data.form.role === 'ADMIN' || data.form.role === 'LIBRARIAN') {
            router.push('/manager/home')
          } else {
            router.push('/front/home')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e8f4f8 0%, #b8d4e8 50%, #7fb8d8 100%);
  position: relative;
  overflow: hidden;
}
.login-bg {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(100, 180, 220, 0.15) 0%, transparent 70%);
  top: -200px;
  right: -100px;
}
.login-card {
  width: 400px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 80, 150, 0.15);
  padding: 40px;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(10px);
}
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-logo img {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-bottom: 10px;
}
.login-title {
  font-size: 24px;
  font-weight: 700;
  color: #1a5a8a;
  letter-spacing: 4px;
}
.login-subtitle {
  font-size: 12px;
  color: #8ab4d0;
  letter-spacing: 2px;
  margin-top: 5px;
}
.login-form {
  margin-top: 20px;
}
.login-form :deep(.el-input__wrapper) {
  background: #f5f9fc;
  box-shadow: none;
  border: 1px solid #dce8f0;
  border-radius: 8px;
}
.login-form :deep(.el-input__wrapper:hover) {
  border-color: #7fb8d8;
}
.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #4a90b8;
  box-shadow: none;
}
.login-form :deep(.el-select) {
  width: 100%;
}
.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
  border-radius: 8px;
  background: linear-gradient(135deg, #4a90b8, #2a6a92);
  border: none;
}
.login-btn:hover {
  background: linear-gradient(135deg, #5aa0c8, #3a7aa2);
}
.login-footer {
  text-align: center;
  color: #8ab4d0;
  font-size: 13px;
}
.login-footer a {
  color: #4a90b8;
  font-weight: 500;
  text-decoration: none;
}
.login-footer a:hover {
  color: #2a6a92;
}
</style>
