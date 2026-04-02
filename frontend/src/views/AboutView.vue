<template>
  <div class="page-grid">
    <section class="page-section">
      <p class="section-tag">About</p>
      <h2>关于我</h2>
      <p class="section-copy">
        后端开发工程师，专注于 Java / Spring Boot 生态，对 AI 辅助开发有持续实践。
        这个站点是我的个人主页 + 技术博客，用于沉淀项目经验和技术思考。
      </p>
    </section>

    <section class="page-section">
      <p class="section-tag">Tech Stack</p>
      <h2>技术栈</h2>
      <div class="about-grid">
        <article v-for="item in techStack" :key="item.label" class="about-card">
          <strong>{{ item.label }}</strong>
          <span>{{ item.detail }}</span>
        </article>
      </div>
    </section>

    <section class="page-section">
      <p class="section-tag">Contact</p>
      <h2>联系方式</h2>
      <p class="section-copy">
        如果你对我的项目或文章有任何想法，欢迎通过以下方式联系我。
      </p>
      <div class="about-grid">
        <article class="about-card">
          <strong>GitHub</strong>
          <span>github.com/your-username</span>
        </article>
        <article class="about-card">
          <strong>Email</strong>
          <span>your-email@example.com</span>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
const techStack = [
  { label: 'Java 17', detail: '主力后端语言' },
  { label: 'Spring Boot 3', detail: 'Web / JPA / Security' },
  { label: 'MySQL', detail: '关系型数据库' },
  { label: 'Vue 3', detail: '前端框架' },
  { label: 'Docker', detail: '容器化部署' },
  { label: 'AI Coding', detail: 'Claude / GPT 辅助开发' },
]
</script>
