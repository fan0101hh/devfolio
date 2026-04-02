<template>
  <div class="page-grid">
    <section class="page-section form-card">
      <div>
        <p class="section-tag">Admin Workspace</p>
        <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
      </div>

      <p v-if="loadError" class="state-message error-text">{{ loadError }}</p>

      <form v-else class="form-stack" @submit.prevent="handleSubmit">
        <div class="field-group">
          <label for="title">标题</label>
          <input id="title" v-model.trim="form.title" type="text" placeholder="文章标题" />
        </div>

        <div class="field-group">
          <label for="slug">Slug</label>
          <input id="slug" v-model.trim="form.slug" type="text" placeholder="article-slug" />
        </div>

        <div class="field-group">
          <label for="summary">摘要</label>
          <textarea id="summary" v-model.trim="form.summary" rows="2" placeholder="文章摘要（最多320字）"></textarea>
        </div>

        <div class="field-group">
          <label for="content">内容</label>
          <textarea id="content" v-model="form.content" rows="14" placeholder="文章正文"></textarea>
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
            <label>标签</label>
            <div class="checkbox-group">
              <label v-for="tag in allTags" :key="tag.id" class="checkbox-label">
                <input type="checkbox" :value="tag.id" v-model="form.tagIds" />
                {{ tag.name }}
              </label>
              <span v-if="!allTags.length" class="content-meta">暂无标签，请先在标签管理中创建</span>
            </div>
          </div>
        </div>

        <p v-if="formMessage" :class="['state-message', formError ? 'error-text' : '']">{{ formMessage }}</p>

        <div class="button-row">
          <button class="primary-button" type="submit" :disabled="submitting">
            {{ submitting ? '提交中...' : isEdit ? '更新文章' : '创建文章' }}
          </button>
          <RouterLink to="/admin/articles" class="ghost-button">返回列表</RouterLink>
        </div>
      </form>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminArticle, createArticle, updateArticle, listAdminTags } from '../lib/api'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => Boolean(route.params.id))
const allTags = ref([])
const submitting = ref(false)
const formMessage = ref('')
const formError = ref(false)
const loadError = ref('')

const form = reactive({
  title: '',
  slug: '',
  summary: '',
  content: '',
  status: 'DRAFT',
  publishedAt: null,
  tagIds: [],
})

async function loadData() {
  try {
    allTags.value = await listAdminTags()

    if (isEdit.value) {
      const article = await getAdminArticle(route.params.id)
      form.title = article.title
      form.slug = article.slug
      form.summary = article.summary
      form.content = article.content
      form.status = article.status
      form.publishedAt = article.publishedAt
      form.tagIds = article.tags.map((t) => t.id)
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
    const payload = { ...form }
    if (isEdit.value) {
      await updateArticle(route.params.id, payload)
      formMessage.value = '文章已更新'
    } else {
      await createArticle(payload)
      router.push('/admin/articles')
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
