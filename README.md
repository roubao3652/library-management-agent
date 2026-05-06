# 图书管理系统

> **Agent 开发说明**：本项目使用 **Claude Code (Agent 编排层) + DeepSeek API (底层推理模型)** 协作完成。

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Vite + Element Plus |
| 后端 | Spring Boot + MyBatis |
| 数据库 | MySQL 8.0 |
| AI Agent | Claude Code (任务拆解 + 审查) + DeepSeek API (代码生成) |

---

## 核心功能

- 图书借阅/归还
- 用户权限管理
- 借阅记录查询

---

## AI Agent 开发流程

本项目非纯手写，而是通过 **Claude Code + DeepSeek** 协作完成：

1. **任务拆解**：Claude Code 将需求拆解为 10 个子任务（后端接口、前端页面、数据库设计等）
2. **代码生成**：DeepSeek 按规范生成 Spring Boot Controller/Service/Mapper 及 Vue 组件
3. **代码审查**：Agent 自动校验代码一致性、安全性
4. **效率提升**：单模块开发周期从 2 天压缩至 4 小时
