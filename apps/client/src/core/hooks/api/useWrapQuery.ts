import { useCallback, useEffect, useRef } from "react";
import { useErrAPI } from "./useErrAPI";
import { useWrapClientListener } from "../etc/hydration/useWrapClientListener";
import { __cg } from "@/core/lib/log";
import { ResApiT } from "@/common/types/api";
import { useMsgAPI } from "./useMsgAPI";

/* eslint-disable @typescript-eslint/no-explicit-any */
type Params<T extends Record<string, any> | void> = {
  hideErr?: boolean;
  showToast?: boolean;
  throwErr?: boolean;

  isSuccess?: boolean;
  isError?: boolean;
  error?: any;
  data?: ResApiT<T>;
  pushNotice?: number[] | "*";
};

export const useWrapQuery = <T extends Record<string, any> | void>({
  showToast = false,
  hideErr,
  throwErr,
  isSuccess,
  isError,
  error,
  data,
  pushNotice,
}: Params<T>) => {
  const { handleErr } = useErrAPI();
  const hasRun = useRef(false);

  const { wrapClientListener } = useWrapClientListener();
  const { handleMsgSession } = useMsgAPI();

  const handleQuery = useCallback(() => {
    if (hasRun.current) return;
    if (isSuccess || isError) hasRun.current = true;

    if (isSuccess) {
      __cg("wrapper query api", data);

      handleMsgSession({ data: data as ResApiT<T>["data"], showToast });
    } else if (isError) {
      handleErr({ err: error, hideErr, throwErr, pushNotice });
    }
  }, [
    handleErr,
    showToast,
    hideErr,
    isSuccess,
    isError,
    error,
    data,
    throwErr,
    handleMsgSession,
    pushNotice,
  ]);

  useEffect(() => {
    wrapClientListener(handleQuery);
  }, [handleQuery, wrapClientListener]);

  const triggerRef = useCallback(() => (hasRun.current = false), []);

  return {
    triggerRef,
  };
};
