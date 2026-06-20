import axios from 'axios'

// Empty base URL = relative requests against whatever host/port the page was
// loaded from (e.g. http://<server-ip>:3000/api/...). Nginx (see nginx.conf)
// proxies /api/* to the api-gateway container on the Docker network, so this
// works the same on localhost, an EC2 public IP, or behind a real domain —
// no rebuild needed per environment. Override with VITE_API_BASE_URL only if
// you need to point the frontend at a gateway running somewhere else entirely.
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || ''

const api = axios.create({ baseURL: API_BASE_URL })

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const isLoginRequest = error.config?.url?.includes('/api/auth/login')
    if (error.response && error.response.status === 401 && !isLoginRequest) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
