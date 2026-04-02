import { ref } from 'vue'

const TOKEN_STORAGE_KEY = 'devfolio.admin.token'

const initialToken = typeof window === 'undefined'
  ? ''
  : window.localStorage.getItem(TOKEN_STORAGE_KEY) || ''

export const authToken = ref(initialToken)

export function getAuthToken() {
  return authToken.value
}

export function setAuthToken(token) {
  authToken.value = token
  window.localStorage.setItem(TOKEN_STORAGE_KEY, token)
}

export function clearAuthToken() {
  authToken.value = ''
  window.localStorage.removeItem(TOKEN_STORAGE_KEY)
}
