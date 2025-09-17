import { DataApiT } from "@/common/types/api";
import { AxiosRequestConfig } from "axios";

export type MethodHttpT = "GET" | "POST" | "PATCH" | "PUT" | "DELETE";

export type BaseQueryArgT = {
  url: string;
  method: MethodHttpT;
  data?: AxiosRequestConfig["data"];
  params?: AxiosRequestConfig["params"];
  responseType?: AxiosRequestConfig["responseType"];
};

export type BaseQueryReturnT = {
  data: DataApiT;
};
