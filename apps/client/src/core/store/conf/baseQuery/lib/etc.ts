/* eslint-disable @typescript-eslint/no-explicit-any */
import { __cg } from "@/core/lib/log";
import {
  AxiosError,
  AxiosResponseHeaders,
  RawAxiosResponseHeaders,
} from "axios";

export const extractHeaders = (
  headers?: AxiosResponseHeaders | RawAxiosResponseHeaders
) => ({
  headers: {
    "ratelimit-limit": headers?.["ratelimit-limit"] ?? null,
    "ratelimit-remaining": headers?.["ratelimit-remaining"] ?? null,
    "ratelimit-window": headers?.["ratelimit-window"] ?? null,
    "ratelimit-reset": headers?.["ratelimit-reset"] ?? null,
  },
});

export const extractMsgErr = (errData: any) =>
  errData?.msg ??
  errData?.message ??
  "A wild Snorlax is fast asleep blocking the road 💤. Try later";

export const parseErr = async (err: any) => {
  const { response } = (err ?? {}) as AxiosError<any>;

  const status = response?.status;

  let errData: any = response?.data ?? {};

  if (errData instanceof Blob && errData.type === "application/json") {
    try {
      const text = await errData.text();
      errData = JSON.parse(text);
    } catch (parseErr: any) {
      __cg("Failed parse blob error", parseErr);
    }
  }

  return {
    response,
    errData,
    status,
  };
};
