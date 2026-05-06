<template>
  <div class="register-container">
    <div class="register-bg"></div>
    <div class="register-card">
      <div class="register-header">
        <div class="register-logo">
          <img src="@/assets/imgs/logo.png" alt="">
        </div>
        <div class="register-title">创建账号</div>
        <div class="register-subtitle">加入图书管理系统</div>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" class="register-form">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <el-input :prefix-icon="User" size="large" v-model="data.form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="register-btn" @click="register">注 册</el-button>
        </el-form-item>
        <div class="register-footer">
          已有账号？请 <a href="/login">登录</a>
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

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error("确认密码跟原密码不一致!"))
  } else {
    callback()
  }
}

const data = reactive({
  form: { role: 'READER' },
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    confirmPassword: [{ validator: validatePass, trigger: 'blur' }]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功')
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e8f4f8 0%, #b8d4e8 50%, #7fb8d8 100%);
  position: relative;
  overflow: hidden;
}
.register-bg {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(100, 180, 220, 0.15) 0%, transparent 70%);
  bottom: -200px;
  left: -100px;
}
.register-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 80, 150, 0.15);
  padding: 40px;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(10px);
}
.register-header {
  text-align: center;
  margin-bottom: 25px;
}
.register-logo img {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  margin-bottom: 8px;
}
.register-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a5a8a;
}
.register-subtitle {
  font-size: 12px;
  color: #8ab4d0;
  margin-top: 3px;
}
.register-form {
  margin-top: 15px;
}
.register-form :deep(.el-input__wrapper) {
  background: #f5f9fc;
  box-shadow: none;
  border: 1px solid #dce8f0;
  border-radius: 8px;
}
.register-form :deep(.el-input__wrapper:hover) {
  border-color: #7fb8d8;
}
.register-form :deep(.el-input__wrapper.is-focus) {
  border-color: #4a90b8;
  box-shadow: none;
}
.register-form :deep(.el-select) {
  width: 100%;
}
.register-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
  border-radius: 8px;
  background: linear-gradient(135deg, #4a90b8, #2a6a92);
  border: none;
}
.register-btn:hover {
  background: linear-gradient(135deg, #5aa0c8, #3a7aa2);
}
.register-footer {
  text-align: center;
  color: #8ab4d0;
  font-size: 13px;
}
.register-footer a {
  color: #4a90b8;
  font-weight: 500;
  text-decoration: none;
}
.register-footer a:hover {
  color: #2a6a92;
}
</style>
