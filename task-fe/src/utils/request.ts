import axios from "axios";
import { Message, MessageBox } from "element-ui";
import { getToken } from "@/utils/auth";

const service = axios.create({
  baseURL: process.env.VUE_APP_MOCK_API,
  timeout: 5000
});

// Request interceptors
service.interceptors.request.use(
  config => {
    // Add X-Token header to every request, you can add other custom headers here

    return config;
  },
  error => {
    Promise.reject(error);
  }
);

// Response interceptors
service.interceptors.response.use(
  response => {
    // Some example codes here:
    // code == 20000: valid
    // code == 50008: invalid token
    // code == 50012: already login in other place
    // code == 50014: token expired
    // code == 60204: account or password is incorrect
    // You can change this part for your own usage.
    const res = response.data;
    if (res.code !== 20000) {
      Message({
        message: res.message,
        type: "error",
        duration: 5 * 1000
      });

      return Promise.reject(new Error("error with code: " + res.code));
    } else {
      return response.data;
    }
  },
  error => {
    Message({
      message: error.message,
      type: "error",
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service;
