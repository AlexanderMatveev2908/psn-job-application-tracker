import { MethodHttpT } from "@/core/store/conf/baseQuery/lib/types";
import { AxiosRequestConfig } from "axios";

/* eslint-disable @typescript-eslint/no-explicit-any */

export enum TagAPI {
  TEST = "TEST",
  WAKE_UP = "WAKE_UP",
  USER = "USER",
  JOB_APPLICATIONS = "JOB_APPLICATIONS",
}

export type AppEventT = "OK" | "INFO" | "WARN" | "ERR" | "NONE";

export type ReqApiT<T extends Record<string, any> | void> = T extends void
  ? {
      _?: number;
    }
  : {
      _?: number;
    } & T;

export type HeadersT = {
  "ratelimit-limit": string;
  "ratelimit-remaining": string;
  "ratelimit-window": string;
  "ratelimit-reset": string;
};
export type ConfApiT = {
  url: string;
  params: AxiosRequestConfig["params"];
  responseType: AxiosRequestConfig["responseType"];
  headers: HeadersT;
  jwt?: string;
  method: MethodHttpT;
  reqData: any;
};

export type DataApiT = {
  conf?: ConfApiT;
  status?: number;
  msg?: string;
  refreshed?: boolean;
  refreshFailed?: boolean;
  restoredSession?: boolean;
  access_token?: string;
  blob?: Blob;
};

export type ResApiT<T> = T extends void
  ? { data: DataApiT }
  : { data: DataApiT } & T;

export type UnwrappedResApiT<T> = ResApiT<T>["data"] & T;

export type TriggerApiT<T> = (arg: any) => {
  unwrap: () => Promise<UnwrappedResApiT<T>>;
};

export type JwtReturnT = {
  access_token: string;
};

export type CbcHmacReturnT = {
  cbc_hmac_token: string;
};
