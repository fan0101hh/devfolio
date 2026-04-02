<template>
  <div class="admin-grid">
    <section class="page-section form-card">
      <div>
        <p class="section-tag">Admin Workspace</p>
        <h2>标签管理</h2>
        <p class="section-copy">这里先做最小后台能力，供文章和项目编辑页复用标签数据。</p>
      </div>

      <form class="form-stack" @submit.prevent="handleSubmit">
        <div class="field-group">
          <label for="tag-name">标签名称</label>
          <input id="tag-name" v-model.trim="form.name" type="text" placeholder="Spring Boot" />
        </div>

        <div class="field-group">
          <label for="tag-slug">标签 Slug</label>
          <input id="tag-slug" v-model.trim="form.slug" type="text" placeholder="spring-boot" />
        </div>

        <p v-if="formMessage" :class="['state-message', formError ? 'error-text' : '']">
          {{ formMessage }}
        </p>

        <div class="button-row">
          <button class="primary-button" type="submit" :disabled="submitting">
            {{ submitting ? '提交中...' : editingId ? '更新标签' : '创建标签' }}
          </button>
          <button v-if="editingId" class="ghost-button" type="button" @click="resetForm">取消编辑</button>
        </div>
      </form>
    </section>

    <section class="page-section table-card">
      <div class="section-heading">
        <div>
          <p class="section-tag">Tag List</p>
          <h2>已创建标签</h2>
        </div>
        <button class="ghost-button" type="button" @click="loadTags" :disabled="loading">
          {{ loading ? '加载中...' : '刷新列表' }}
        </button>
      </div>

      <p v-if="listMessage" :class="['state-message', listError ? 'error-text' : '']">
        {{ listMessage }}
      </p>

      <div v-else-if="tags.length" class="table-list">
        <article v-for="tag in tags" :key="tag.id" class="table-row">
          <div>
            <p class="content-meta">ID {{ tag.id }}</p>
            <h3>{{ tag.name }}</h3>
            <p class="section-copy">{{ tag.slug }}</p>
          </div>
          <div class="row-actions">
            <button class="ghost-button" type="button" @click="startEdit(tag)">编辑</button>
            <button class="danger-button" type="button" @click="handleDelete(tag.id)">删除</button>
          </div>
        </article>
      </div>
      <p v-else class="state-message">还没有标签，先创建一个用于测试。</p>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createTag, deleteTag, listAdminTags, updateTag } from '../lib/api'

const tags = ref([])
const loading = ref(false)
const submitting = ref(false)
const editingId = ref(null)
const listMessage = ref('')
const listError = ref(false)
const formMessage = ref('')
const formError = ref(false)

const form = reactive({
  name: '',
  slug: '',
})

async function loadTags() {
  loading.value = true
  listMessage.value = ''
  listError.value = false

  try {
    tags.value = await listAdminTags()
  } catch (error) {
    listMessage.value = error.message || '标签加载失败'
    listError.value = true
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  submitting.value = true
  formMessage.value = ''
  formError.value = false

  try {
    if (editingId.value) {
      await updateTag(editingId.value, form)
      formMessage.value = '标签已更新'
    } else {
      await createTag(form)
      formMessage.value = '标签已创建'
    }
    resetForm()
    await loadTags()
  } catch (error) {
    formMessage.value = error.message || '提交失败'
    formError.value = true
  } finally {
    submitting.value = false
  }
}

function startEdit(tag) {
  editingId.value = tag.id
  form.name = tag.name
  form.slug = tag.slug
  formMessage.value = ''
  formError.value = false
}

function resetForm() {
  editingId.value = null
  form.name = ''
  form.slug = ''
}

async function handleDelete(id) {
  try {
    await deleteTag(id)
    if (editingId.value === id) {
      resetForm()
    }
    await loadTags()
  } catch (error) {
    listMessage.value = error.message || '删除失败'
    listError.value = true
  }
}

onMounted(() => {
  loadTags()
})
</script>
