<template>
  <div class="page-grid">
    <section class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Admin Workspace</p>
          <h2>项目管理</h2>
        </div>
        <div class="button-row">
          <RouterLink to="/admin/projects/new" class="primary-button">新建项目</RouterLink>
          <button class="ghost-button" type="button" @click="loadProjects" :disabled="loading">
            {{ loading ? '加载中...' : '刷新列表' }}
          </button>
        </div>
      </div>

      <p v-if="message" :class="['state-message', hasError ? 'error-text' : '']">{{ message }}</p>

      <div v-else-if="projects.length" class="table-list">
        <article v-for="item in projects" :key="item.id" class="table-row">
          <div>
            <p class="content-meta">
              ID {{ item.id }}
              · {{ formatDate(item.updatedAt) }}
              <span :class="['status-pill', 'status-' + item.status.toLowerCase()]">{{ item.status }}</span>
              <span v-if="item.featured" class="featured-badge">Featured</span>
            </p>
            <h3>{{ item.name }}</h3>
            <p class="section-copy">{{ item.summary }}</p>
            <div class="tag-list" v-if="item.tags.length">
              <span v-for="tag in item.tags" :key="tag.id" class="tag-pill"># {{ tag.name }}</span>
            </div>
          </div>
          <div class="row-actions">
            <RouterLink :to="`/admin/projects/${item.id}/edit`" class="ghost-button">编辑</RouterLink>
            <button class="danger-button" type="button" @click="handleDelete(item.id)">删除</button>
          </div>
        </article>
      </div>
      <p v-else-if="!loading" class="state-message">暂无项目，点击「新建项目」开始。</p>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listAdminProjects, deleteProject } from '../lib/api'

const projects = ref([])
const loading = ref(false)
const message = ref('')
const hasError = ref(false)

async function loadProjects() {
  loading.value = true
  message.value = ''
  hasError.value = false
  try {
    projects.value = await listAdminProjects()
  } catch (error) {
    message.value = error.message || '加载失败'
    hasError.value = true
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  if (!confirm('确定要删除这个项目吗？')) return
  try {
    await deleteProject(id)
    await loadProjects()
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
  loadProjects()
})
</script>
