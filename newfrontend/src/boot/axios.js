import { boot } from 'quasar/wrappers'
import axios from 'axios'
import setting from 'app/default.json'

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)

const BACKEND_URL1 = setting.url.backendShorten1
const BACKEND_URL2 = setting.url.backendShorten2

const backendServers = [
  BACKEND_URL1,
  BACKEND_URL2
]

let currentServerIndex = 0

const getBackendUrl = () => {
  const url = backendServers[currentServerIndex]
  currentServerIndex = (currentServerIndex + 1) % backendServers.length
  return url
}

const axiosInstance = axios.create({
  baseURL: getBackendUrl()
})

// const api = axios.create({ baseURL: 'https://api.example.com' })

export default boot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  // app.config.globalProperties.$axios = axios
  app.config.globalProperties.$axios = axiosInstance
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  // app.config.globalProperties.$api = api
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
})

export { axiosInstance, getBackendUrl }
