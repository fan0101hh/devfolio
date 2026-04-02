import { getAuthToken, clearAuthToken } from './auth'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {})
  const token = getAuthToken()

  if (!headers.has('Content-Type') && options.body) {
    headers.set('Content-Type', 'application/json')
  }

  if (token && options.auth !== false) {
    headers.set('Authorization', `Bearer ${token}`)
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  })

  if (response.status === 204) {
    return null
  }

  if (response.status === 401 && token) {
    clearAuthToken()
    window.location.href = '/admin/login'
    throw new Error('登录已过期，请重新登录')
  }

  const data = await response.json().catch(() => null)

  if (!response.ok) {
    throw new Error(data?.message || '请求失败')
  }

  return data
}

// ── Public ──

export function listPublicArticles() {
  return request('/public/articles', { auth: false })
}

export function getPublicArticle(slug) {
  return request(`/public/articles/${slug}`, { auth: false })
}

export function listPublicProjects() {
  return request('/public/projects', { auth: false })
}

export function getPublicProject(slug) {
  return request(`/public/projects/${slug}`, { auth: false })
}

export function listPublicTags() {
  return request('/public/tags', { auth: false })
}

// ── Auth ──

export function loginAdmin(payload) {
  return request('/admin/auth/login', {
    method: 'POST',
    auth: false,
    body: JSON.stringify(payload),
  })
}

// ── Admin Tags ──

export function listAdminTags() {
  return request('/admin/tags')
}

export function createTag(payload) {
  return request('/admin/tags', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateTag(id, payload) {
  return request(`/admin/tags/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteTag(id) {
  return request(`/admin/tags/${id}`, {
    method: 'DELETE',
  })
}

// ── Admin Articles ──

export function listAdminArticles() {
  return request('/admin/articles')
}

export function getAdminArticle(id) {
  return request(`/admin/articles/${id}`)
}

export function createArticle(payload) {
  return request('/admin/articles', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateArticle(id, payload) {
  return request(`/admin/articles/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteArticle(id) {
  return request(`/admin/articles/${id}`, {
    method: 'DELETE',
  })
}

// ── Admin Projects ──

export function listAdminProjects() {
  return request('/admin/projects')
}

export function getAdminProject(id) {
  return request(`/admin/projects/${id}`)
}

export function createProject(payload) {
  return request('/admin/projects', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateProject(id, payload) {
  return request(`/admin/projects/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteProject(id) {
  return request(`/admin/projects/${id}`, {
    method: 'DELETE',
  })
}

