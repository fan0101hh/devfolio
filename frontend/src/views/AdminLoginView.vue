<template>
  <section class="page-section admin-login form-card">
    <div>
      <p class="section-tag">Admin Access</p>
      <h2>管理员登录</h2>
      <p class="section-copy">登录后可进入最小管理台，先维护标签，再逐步接文章和项目编辑。</p>
    </div>

    <form class="form-stack" @submit.prevent="handleSubmit">
      <div class="field-group">
        <label for="username">用户名</label>
        <input id="username" v-model.trim="form.username" type="text" placeholder="admin" />
      </div>

      <div class="field-group">
        <label for="password">密码</label>
        <input id="password" v-model="form.password" type="password" placeholder="请输入管理员密码" />
      </div>

      <p v-if="errorMessage" class="state-message error-text">{{ errorMessage }}</p>

      <div class="button-row">
        <button class="primary-button" type="submit" :disabled="submitting">
          {{ submitting ? '登录中...' : '登录' }}
        </button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loginAdmin } from '../lib/api'
import { setAuthToken } from '../lib/auth'

const router = useRouter()
const route = useRoute()

const form = reactive({
  username: 'admin',
  password: '',
})

const submitting = ref(false)
const errorMessage = ref('')

async function handleSubmit() {
  submitting.value = true
  errorMessage.value = ''

  try {
    const result = await loginAdmin(form)
    setAuthToken(result.token)
    router.push(route.query.redirect || '/admin/tags')
  } catch (error) {
    errorMessage.value = error.message || '登录失败'
  } finally {
    submitting.value = false
  }
}
</script>

