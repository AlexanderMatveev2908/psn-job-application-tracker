/* eslint-disable @typescript-eslint/no-explicit-any */
import { ConfApiT } from "@/common/types/api";
import { BaseQueryArgT, BaseQueryReturnT } from "./types";
import { instanceAxs } from "../axiosInstance";
import { clearStorage, saveStorage } from "@/core/lib/storage";
import { extractHeaders, extractMsgErr } from "./etc";
import { __cg } from "@/core/lib/log";
import { AxiosError } from "axios";
import { isStr } from "@/core/lib/dataStructure/ect";

const getFreshAccessToken = async (): Promise<string> => {
  const { data } = await instanceAxs.get("/auth/refresh");
  const accessToken = data?.accessToken;

  if (!isStr(accessToken)) throw new Error("refresh_failed");

  saveStorage(accessToken, { key: "accessToken" });
  instanceAxs.defaults.headers.common[
    "Authorization"
  ] = `Bearer ${accessToken}`;

  return accessToken;
};

export const refreshToken = async ({
  url,
  method,
  data: originalDataRequest,
  params,
  responseType,
  conf,
}: BaseQueryArgT & { conf: Partial<ConfApiT> }) => {
  const accessToken = await getFreshAccessToken();

  if (!isStr(accessToken)) throw new Error("refresh_failed");

  const {
    data: dataRetry,
    status,
    headers,
  } = await instanceAxs({
    url,
    method,
    data: originalDataRequest,
    params,
    responseType,
  });

  __cg("refresh access");

  const confWithHeaders = {
    ...conf,
    ...extractHeaders(headers),
  } as ConfApiT;

  const resultRetryReturn: BaseQueryReturnT = {
    data: {
      conf: confWithHeaders,
      status,
      refreshed: true,
      accessToken: accessToken,
    },
  };

  if (responseType === "blob" && dataRetry instanceof Blob)
    resultRetryReturn.data.blob = dataRetry;
  else
    resultRetryReturn.data = {
      ...resultRetryReturn.data,
      ...dataRetry,
    };

  return resultRetryReturn;
};

export const handleRefreshErr = ({
  err,
  conf,
}: {
  err: any;
  conf: Partial<ConfApiT>;
}) => {
  const { response } = (err ?? {}) as AxiosError<any>;

  const status = response?.status;
  const dataFail = response?.data ?? {};

  const confWithHeaders = {
    ...conf,
    ...extractHeaders(response?.headers),
  };

  const refreshFailed =
    status === 401 &&
    ["jwe_expired", "jwe_invalid", "jwe_not_provided", "jwe_not_found"].some(
      (txt) => (dataFail?.msg ?? "")?.includes(txt)
    );

  if (refreshFailed) {
    clearStorage();
    delete instanceAxs.defaults.headers.common.Authorization;

    __cg("refresh failed");
  }

  return {
    error: {
      ...dataFail,
      conf: confWithHeaders,
      msg: extractMsgErr(dataFail),
      status,
      refreshFailed,
    },
  };
};
