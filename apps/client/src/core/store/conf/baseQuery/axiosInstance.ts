import { envApp } from "@/core/constants/env";
import { isStr } from "@/core/lib/dataStructure/ect";
import { getStorage } from "@/core/lib/storage";
import axios from "axios";

export const instanceAxs = axios.create({
  baseURL: envApp.BACK_URL,
  withCredentials: true,
});

instanceAxs.interceptors.request.use((config) => {
  const accessToken = getStorage("accessToken");

  if (isStr(accessToken))
    config.headers["Authorization"] = `Bearer ${accessToken}`;

  config.headers["Content-Type"] =
    config.data instanceof FormData
      ? "multipart/form-data"
      : "application/json";
  return config;
});
