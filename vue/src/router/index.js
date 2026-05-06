import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue') },
        { path: 'admin', meta: { name: '管理员管理' }, component: () => import('@/views/manager/Admin.vue') },
        { path: 'user', meta: { name: '用户管理' }, component: () => import('@/views/manager/User.vue') },
        { path: 'bookCategory', meta: { name: '图书分类' }, component: () => import('@/views/manager/BookCategory.vue') },
        { path: 'book', meta: { name: '图书管理' }, component: () => import('@/views/manager/Book.vue') },
        { path: 'borrowing', meta: { name: '借阅管理' }, component: () => import('@/views/manager/Borrowing.vue') },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue') },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue') },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', meta: { name: '首页' }, component: () => import('@/views/front/Home.vue') },
        { path: 'books', meta: { name: '图书浏览' }, component: () => import('@/views/front/Books.vue') },
        { path: 'bookDetail', meta: { name: '图书详情' }, component: () => import('@/views/front/BookDetail.vue') },
        { path: 'myBorrow', meta: { name: '我的借阅' }, component: () => import('@/views/front/MyBorrow.vue') },
        { path: 'person', meta: { name: '个人中心' }, component: () => import('@/views/front/Person.vue') },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/front/Password.vue') },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router
