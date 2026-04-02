<template>
  <div class="page-grid">
    <section class="page-section form-card">
      <div>
        <p class="section-tag">Admin Workspace</p>
        <h2>{{ isEdit ? '编辑项目' : '新建项目' }}</h2>
      </div>

      <p v-if="loadError" class="state-message error-text">{{ loadError }}</p>

      <form v-else class="form-stack" @submit.prevent="handleSubmit">
        <div class="field-group">
          <label for="name">项目名称</label>
          <input id="name" v-model.trim="form.name" type="text" placeholder="项目名称" />
        </div>

        <div class="field-group">
          <label for="slug">Slug</label>
          <input id="slug" v-model.trim="form.slug" type="text" placeholder="project-slug" />
        </div>

        <div class="field-group">
          <label for="summary">摘要</label>
          <textarea id="summary" v-model.trim="form.summary" rows="2" placeholder="项目摘要（最多320字）"></textarea>
        </div>

        <div class="field-group">
          <label for="description">详细描述</label>
          <textarea id="description" v-model="form.description" rows="10" placeholder="项目详细描述"></textarea>
        </div>

        <div class="form-row-2">
          <div class="field-group">
            <label for="repoUrl">仓库地址</label>
            <input id="repoUrl" v-model.trim="form.repoUrl" type="url" placeholder="https://github.com/..." />
          </div>

          <div class="field-group">
            <label for="demoUrl">演示地址</label>
            <input id="demoUrl" v-model.trim="form.demoUrl" type="url" placeholder="https://..." />
          </div>
        </div>

        <div class="form-row-2">
          <div class="field-group">
            <label for="status">状态</label>
            <select id="status" v-model="form.status">
              <option value="DRAFT">草稿</option>
              <option value="PUBLISHED">已发布</option>
            </select>
          </div>

          <div class="field-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="form.featured" />
              设为精选项目
            </label>
          </div>
        </div>

        <div class="field-group">
          <label>标签</label>
          <div class="checkbox-group">
            <label v-for="tag in allTags" :key="tag.id" class="checkbox-label">
              <input type="checkbox" :value="tag.id" v-model="form.tagIds" />
              {{ tag.name }}
            </label>
            <span v-if="!allTags.length" class="content-meta">暂无标签，请先在标签管理中创建</span>
          </div>
        </div>

        <p v-if="formMessage" :class="['state-message', formError ? 'error-text' : '']">{{ formMessage }}</p>

        <div class="button-row">
          <button class="primary-button" type="submit" :disabled="submitting">
            {{ submitting ? '提交中...' : isEdit ? '更新项目' : '创建项目' }}
          </button>
          <RouterLink to="/admin/projects" class="ghost-button">返回列表</RouterLink>
        </div>
      </form>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminProject, createProject, updateProject, listAdminTags } from '../lib/api'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => Boolean(route.params.id))
const allTags = ref([])
const submitting = ref(false)
const formMessage = ref('')
const formError = ref(false)
const loadError = ref('')

const form = reactive({
  name: '',
  slug: '',
  summary: '',
  description: '',
  repoUrl: '',
  demoUrl: '',
  featured: false,
  status: 'DRAFT',
  publishedAt: null,
  tagIds: [],
})

async function loadData() {
  try {
    allTags.value = await listAdminTags()

    if (isEdit.value) {
      const project = await getAdminProject(route.params.id)
      form.name = project.name
      form.slug = project.slug
      form.summary = project.summary
      form.description = project.description
      form.repoUrl = project.repoUrl || ''
      form.demoUrl = project.demoUrl || ''
      form.featured = project.featured
      form.status = project.status
      form.publishedAt = project.publishedAt
      form.tagIds = project.tags.map((t) => t.id)
    }
  } catch (error) {
    loadError.value = error.message || '加载失败'
  }
}

async function handleSubmit() {
  submitting.value = true
  formMessage.value = ''
  formError.value = false

  try {
    const payload = {
      ...form,
      repoUrl: form.repoUrl || null,
      demoUrl: form.demoUrl || null,
    }
    if (isEdit.value) {
      await updateProject(route.params.id, payload)
      formMessage.value = '项目已更新'
    } else {
      await createProject(payload)
      router.push('/admin/projects')
    }
  } catch (error) {
    formMessage.value = error.message || '提交失败'
    formError.value = true
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
