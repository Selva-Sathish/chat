import axios from "axios";
import  type  {AxiosInstance, AxiosError, AxiosRequestConfig, InternalAxiosRequestConfig} from "axios"; 
import { getToken, setToken, clearToken } from "../service/TokenService";

interface FailedRequest{
    resolve: (value?: unknown) => void,
    reject: (error: any) => void;
}

declare module "axios" {
  export interface AxiosRequestConfig {
    _retry?: boolean;
  }
}

let isResfreshing: boolean = false;
let failedQueue: FailedRequest[] = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

const request = axios.create(
    {
        baseURL: "http://localhost:8080",
        headers: {"Content-Type": "application/json"},
        withCredentials: true
    }
)

request.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const token = getToken();
    if(token){
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
},
    (error) => Promise.reject(error)
);

export default request;