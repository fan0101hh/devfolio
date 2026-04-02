<template>
  <div class="app-shell">
    <header class="site-header">
      <div class="site-brand">
        <p class="eyebrow">DevFolio</p>
        <h1>后端开发者的个人主页、项目集与技术博客</h1>
        <p class="brand-copy">
          聚焦后端工程、AI 协作开发和个人项目沉淀。
        </p>
      </div>
      <div class="site-actions">
        <nav class="site-nav">
          <RouterLink to="/">首页</RouterLink>
          <RouterLink to="/projects">项目</RouterLink>
          <RouterLink to="/articles">文章</RouterLink>
          <RouterLink to="/about">关于</RouterLink>
        </nav>
        <div class="admin-actions">
          <RouterLink v-if="!isAuthenticated" class="admin-link" to="/admin/login">管理员登录</RouterLink>
          <template v-else>
            <RouterLink class="admin-link" to="/admin/articles">文章管理</RouterLink>
            <RouterLink class="admin-link" to="/admin/projects">项目管理</RouterLink>
            <RouterLink class="admin-link" to="/admin/tags">标签管理</RouterLink>
            <button class="ghost-button" type="button" @click="logout">退出</button>
          </template>
        </div>
      </div>
    </header>

    <main class="page-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { authToken, clearAuthToken } from './lib/auth'

const router = useRouter()
const isAuthenticated = computed(() => Boolean(authToken.value))

function logout() {
  clearAuthToken()
  router.push('/admin/login')
}
</script>
