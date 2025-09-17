import { envApp } from "@/core/constants/env";
import { isStr } from "@/core/lib/dataStructure/ect";
import { getStorage } from "@/core/lib/storage";
import axios from "axios";

export const instanceAxs = axios.create({
  baseURL: envApp.BACK_URL,
  withCredentials: true,
});

instanceAxs.interceptors.request.use((config) => {
  const access_token = getStorage("access_token");

  if (isStr(access_token))
    config.headers["Authorization"] = `Bearer ${access_token}`;

  config.headers["Content-Type"] =
    config.data instanceof FormData
      ? "multipart/form-data"
      : "application/json";
  return config;
});
