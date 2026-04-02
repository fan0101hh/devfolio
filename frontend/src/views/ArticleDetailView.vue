<template>
  <div class="page-grid">
    <section v-if="errorMessage" class="page-section">
      <p class="state-message error-text">{{ errorMessage }}</p>
      <RouterLink to="/articles" class="ghost-button">返回文章列表</RouterLink>
    </section>

    <section v-else-if="loading" class="page-section">
      <p class="state-message">正在加载文章...</p>
    </section>

    <template v-else-if="article">
      <section class="page-section detail-header">
        <RouterLink to="/articles" class="back-link">← 返回文章列表</RouterLink>
        <p class="section-tag">Article</p>
        <h2>{{ article.title }}</h2>
        <p class="content-meta">
          发布于 {{ formatDate(article.publishedAt) }}
          · 更新于 {{ formatDate(article.updatedAt) }}
        </p>
        <div class="tag-list">
          <span v-for="tag in article.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
        </div>
      </section>

      <section class="page-section">
        <div class="article-content" v-text="article.content"></div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getPublicArticle } from '../lib/api'

const route = useRoute()
const article = ref(null)
const loading = ref(false)
const errorMessage = ref('')

async function loadArticle(slug) {
  loading.value = true
  errorMessage.value = ''
  try {
    article.value = await getPublicArticle(slug)
  } catch (error) {
    errorMessage.value = error.message || '文章不存在'
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
  loadArticle(route.params.slug)
})

watch(() => route.params.slug, (slug) => {
  if (slug) loadArticle(slug)
})
</script>
