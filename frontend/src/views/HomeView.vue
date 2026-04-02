<template>
  <div class="page-grid">
    <section class="page-section hero">
      <p class="section-tag">Backend Engineer / AI Builder</p>
      <h2>用后端工程能力构建个人品牌和技术输出。</h2>
      <p class="section-copy">
        这里将承载你的个人简介、代表项目、技术文章和 AI 工程实践。
      </p>
      <div class="hero-metrics">
        <article>
          <strong>{{ articles.length }}</strong>
          <span>已发布文章</span>
        </article>
        <article>
          <strong>{{ projects.length }}</strong>
          <span>已发布项目</span>
        </article>
        <article>
          <strong>{{ featuredProjects.length }}</strong>
          <span>精选项目</span>
        </article>
      </div>
    </section>

    <section v-if="featuredProjects.length" class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Featured Projects</p>
          <h2>精选项目</h2>
        </div>
        <RouterLink to="/projects" class="ghost-button">查看全部</RouterLink>
      </div>
      <div class="content-list">
        <RouterLink
          v-for="project in featuredProjects"
          :key="project.id"
          :to="`/projects/${project.slug}`"
          class="content-card content-card-link"
        >
          <div class="content-card-head">
            <div>
              <p class="content-meta">
                {{ formatDate(project.publishedAt) }}
                <span class="featured-badge">Featured</span>
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
    </section>

    <section class="page-section">
      <div class="section-heading">
        <div>
          <p class="section-tag">Latest Articles</p>
          <h2>最新文章</h2>
        </div>
        <RouterLink to="/articles" class="ghost-button">查看全部</RouterLink>
      </div>

      <p v-if="errorMessage" class="state-message error-text">{{ errorMessage }}</p>
      <p v-else-if="loading" class="state-message">正在加载...</p>
      <div v-else-if="articles.length" class="content-list">
        <RouterLink
          v-for="article in articles.slice(0, 5)"
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
import { computed, onMounted, ref } from 'vue'
import { listPublicArticles, listPublicProjects } from '../lib/api'

const articles = ref([])
const projects = ref([])
const loading = ref(false)
const errorMessage = ref('')

const featuredProjects = computed(() => projects.value.filter((p) => p.featured))

async function loadData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [articleData, projectData] = await Promise.all([
      listPublicArticles(),
      listPublicProjects(),
    ])
    articles.value = articleData
    projects.value = projectData
  } catch (error) {
    errorMessage.value = error.message || '加载失败'
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
  loadData()
})
</script>
