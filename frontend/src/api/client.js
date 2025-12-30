// Frontend/src/api/client.js
import axios from 'axios'

export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: false
})

export const setAuthHeader = (token) => {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    delete api.defaults.headers.common['Authorization']
  }
}