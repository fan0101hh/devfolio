import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import ProjectDetailView from '../views/ProjectDetailView.vue'
import ArticlesView from '../views/ArticlesView.vue'
import ArticleDetailView from '../views/ArticleDetailView.vue'
import AboutView from '../views/AboutView.vue'
import AdminLoginView from '../views/AdminLoginView.vue'
import AdminTagsView from '../views/AdminTagsView.vue'
import AdminArticlesView from '../views/AdminArticlesView.vue'
import AdminArticleFormView from '../views/AdminArticleFormView.vue'
import AdminProjectsView from '../views/AdminProjectsView.vue'
import AdminProjectFormView from '../views/AdminProjectFormView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import { getAuthToken } from '../lib/auth'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/projects',
    name: 'projects',
    component: ProjectsView,
  },
  {
    path: '/projects/:slug',
    name: 'project-detail',
    component: ProjectDetailView,
  },
  {
    path: '/articles',
    name: 'articles',
    component: ArticlesView,
  },
  {
    path: '/articles/:slug',
    name: 'article-detail',
    component: ArticleDetailView,
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView,
  },
  {
    path: '/admin/login',
    name: 'admin-login',
    component: AdminLoginView,
    meta: {
      guestOnly: true,
    },
  },
  {
    path: '/admin/tags',
    name: 'admin-tags',
    component: AdminTagsView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/articles',
    name: 'admin-articles',
    component: AdminArticlesView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/articles/new',
    name: 'admin-article-new',
    component: AdminArticleFormView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/articles/:id/edit',
    name: 'admin-article-edit',
    component: AdminArticleFormView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/projects',
    name: 'admin-projects',
    component: AdminProjectsView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/projects/new',
    name: 'admin-project-new',
    component: AdminProjectFormView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/admin/projects/:id/edit',
    name: 'admin-project-edit',
    component: AdminProjectFormView,
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: NotFoundView,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

const PAGE_TITLES = {
  home: 'DevFolio',
  articles: '文章 - DevFolio',
  'article-detail': '文章 - DevFolio',
  projects: '项目 - DevFolio',
  'project-detail': '项目 - DevFolio',
  about: '关于 - DevFolio',
  'admin-login': '登录 - DevFolio',
  'admin-tags': '标签管理 - DevFolio',
  'admin-articles': '文章管理 - DevFolio',
  'admin-article-new': '新建文章 - DevFolio',
  'admin-article-edit': '编辑文章 - DevFolio',
  'admin-projects': '项目管理 - DevFolio',
  'admin-project-new': '新建项目 - DevFolio',
  'admin-project-edit': '编辑项目 - DevFolio',
  'not-found': '404 - DevFolio',
}

router.beforeEach((to) => {
  document.title = PAGE_TITLES[to.name] || 'DevFolio'
  const token = getAuthToken()

  if (to.meta.requiresAuth && !token) {
    return {
      name: 'admin-login',
      query: {
        redirect: to.fullPath,
      },
    }
  }

  if (to.meta.guestOnly && token) {
    return { name: 'admin-articles' }
  }

  return true
})

export default router
