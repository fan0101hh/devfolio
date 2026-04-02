<template>
  <div class="page-grid">
    <section class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Admin Workspace</p>
          <h2>文章管理</h2>
        </div>
        <div class="button-row">
          <RouterLink to="/admin/articles/new" class="primary-button">新建文章</RouterLink>
          <button class="ghost-button" type="button" @click="loadArticles" :disabled="loading">
            {{ loading ? '加载中...' : '刷新列表' }}
          </button>
        </div>
      </div>

      <p v-if="message" :class="['state-message', hasError ? 'error-text' : '']">{{ message }}</p>

      <div v-else-if="articles.length" class="table-list">
        <article v-for="item in articles" :key="item.id" class="table-row">
          <div>
            <p class="content-meta">
              ID {{ item.id }}
              · {{ formatDate(item.updatedAt) }}
              <span :class="['status-pill', 'status-' + item.status.toLowerCase()]">{{ item.status }}</span>
            </p>
            <h3>{{ item.title }}</h3>
            <p class="section-copy">{{ item.summary }}</p>
            <div class="tag-list" v-if="item.tags.length">
              <span v-for="tag in item.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
            </div>
          </div>
          <div class="row-actions">
            <RouterLink :to="`/admin/articles/${item.id}/edit`" class="ghost-button">编辑</RouterLink>
            <button class="danger-button" type="button" @click="handleDelete(item.id)">删除</button>
          </div>
        </article>
      </div>
      <p v-else-if="!loading" class="state-message">暂无文章，点击「新建文章」开始创作。</p>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listAdminArticles, deleteArticle } from '../lib/api'

const articles = ref([])
const loading = ref(false)
const message = ref('')
const hasError = ref(false)

async function loadArticles() {
  loading.value = true
  message.value = ''
  hasError.value = false
  try {
    articles.value = await listAdminArticles()
  } catch (error) {
    message.value = error.message || '加载失败'
    hasError.value = true
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  if (!confirm('确定要删除这篇文章吗？')) return
  try {
    await deleteArticle(id)
    await loadArticles()
  } catch (error) {
    message.value = error.message || '删除失败'
    hasError.value = true
  }
}

function formatDate(value) {
  if (!value) return ''
  return new Intl.DateTimeFormat('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  }).format(new Date(value))
}

onMounted(() => {
  loadArticles()
})
</script>
