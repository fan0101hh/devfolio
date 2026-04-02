<template>
  <div class="page-grid">
    <section class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Projects</p>
          <h2>项目</h2>
        </div>
        <button class="ghost-button" type="button" @click="loadProjects" :disabled="loading">
          {{ loading ? '加载中...' : '刷新' }}
        </button>
      </div>

      <p v-if="errorMessage" class="state-message error-text">{{ errorMessage }}</p>
      <p v-else-if="loading" class="state-message">正在加载项目列表...</p>
      <div v-else-if="projects.length" class="content-list">
        <RouterLink
          v-for="project in projects"
          :key="project.id"
          :to="`/projects/${project.slug}`"
          class="content-card content-card-link"
        >
          <div class="content-card-head">
            <div>
              <p class="content-meta">
                {{ formatDate(project.publishedAt) }}
                <span v-if="project.featured" class="featured-badge">Featured</span>
              </p>
              <h3>{{ project.name }}</h3>
            </div>
          </div>
          <p class="content-summary">{{ project.summary }}</p>
          <div class="tag-list">
            <span v-for="tag in project.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
          </div>
        </RouterLink>
      </div>
      <p v-else class="state-message">暂无已发布项目。</p>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listPublicProjects } from '../lib/api'

const projects = ref([])
const loading = ref(false)
const errorMessage = ref('')

async function loadProjects() {
  loading.value = true
  errorMessage.value = ''
  try {
    projects.value = await listPublicProjects()
  } catch (error) {
    errorMessage.value = error.message || '项目加载失败'
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
  loadProjects()
})
</script>
