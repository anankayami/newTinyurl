import { boot } from 'quasar/wrappers';
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: `/api`,
  timeout: 300000,
  withCredentials: true
});
export default boot(({ app }) => {
  app.config.globalProperties.$axios = axiosInstance;
});

export { axiosInstance };
