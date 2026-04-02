<template>
  <div class="page-grid">
    <section class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Articles</p>
          <h2>技术文章</h2>
        </div>
        <button class="ghost-button" type="button" @click="loadArticles" :disabled="loading">
          {{ loading ? '加载中...' : '刷新' }}
        </button>
      </div>

      <p v-if="errorMessage" class="state-message error-text">{{ errorMessage }}</p>
      <p v-else-if="loading" class="state-message">正在加载文章列表...</p>
      <div v-else-if="articles.length" class="content-list">
        <RouterLink
          v-for="article in articles"
          :key="article.id"
          :to="`/articles/${article.slug}`"
          class="content-card content-card-link"
        >
          <div class="content-card-head">
            <div>
              <p class="content-meta">{{ formatDate(article.publishedAt) }}</p>
              <h3>{{ article.title }}</h3>
            </div>
          </div>
          <p class="content-summary">{{ article.summary }}</p>
          <div class="tag-list">
            <span v-for="tag in article.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
          </div>
        </RouterLink>
      </div>
      <p v-else class="state-message">暂无已发布文章。</p>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listPublicArticles } from '../lib/api'

const articles = ref([])
const loading = ref(false)
const errorMessage = ref('')

async function loadArticles() {
  loading.value = true
  errorMessage.value = ''
  try {
    articles.value = await listPublicArticles()
  } catch (error) {
    errorMessage.value = error.message || '文章加载失败'
  } finally {
    loading.value = false
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
