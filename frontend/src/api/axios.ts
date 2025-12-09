import axios, { AxiosError, type AxiosRequestConfig } from "axios";

interface RetryAxiosRequestConfig extends AxiosRequestConfig {
  _retry?: boolean;
}

const request = axios.create({
  baseURL: "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
  withCredentials: true // IMPORTANT: send cookies!
});

let isRefreshing = false;
let failedQueue: Array<{ resolve: Function; reject: Function }> = [];

function processQueue(err: any) {
  failedQueue.forEach(prom => (err ? prom.reject(err) : prom.resolve()));
  failedQueue = [];
}

async function refreshSession(): Promise<void> {
  // Server sets a NEW HttpOnly cookie here
  await request.post("/auth/refresh");
}

request.interceptors.response.use(
  response => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as RetryAxiosRequestConfig;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(() => request(originalRequest)); // retry after refresh
      }

      isRefreshing = true;

      try {
        await refreshSession(); // refresh cookies
        processQueue(null);
        return request(originalRequest); // retry original request
      } catch (err) {
        processQueue(err);
        throw err;
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default request;
