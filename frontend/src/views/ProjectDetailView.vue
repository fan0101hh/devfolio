<template>
  <div class="page-grid">
    <section v-if="errorMessage" class="page-section">
      <p class="state-message error-text">{{ errorMessage }}</p>
      <RouterLink to="/projects" class="ghost-button">返回项目列表</RouterLink>
    </section>

    <section v-else-if="loading" class="page-section">
      <p class="state-message">正在加载项目...</p>
    </section>

    <template v-else-if="project">
      <section class="page-section detail-header">
        <RouterLink to="/projects" class="back-link">← 返回项目列表</RouterLink>
        <p class="section-tag">Project</p>
        <h2>{{ project.name }}</h2>
        <p class="content-meta">
          发布于 {{ formatDate(project.publishedAt) }}
          · 更新于 {{ formatDate(project.updatedAt) }}
        </p>
        <div class="detail-links">
          <a v-if="project.repoUrl" :href="project.repoUrl" target="_blank" rel="noopener" class="ghost-button">
            仓库地址
          </a>
          <a v-if="project.demoUrl" :href="project.demoUrl" target="_blank" rel="noopener" class="ghost-button">
            在线演示
          </a>
        </div>
        <div class="tag-list">
          <span v-for="tag in project.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
        </div>
      </section>

      <section class="page-section">
        <div class="article-content" v-text="project.description"></div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getPublicProject } from '../lib/api'

const route = useRoute()
const project = ref(null)
const loading = ref(false)
const errorMessage = ref('')

async function loadProject(slug) {
  loading.value = true
  errorMessage.value = ''
  try {
    project.value = await getPublicProject(slug)
  } catch (error) {
    errorMessage.value = error.message || '项目不存在'
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
  loadProject(route.params.slug)
})

watch(() => route.params.slug, (slug) => {
  if (slug) loadProject(slug)
})
</script>
