/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback } from "react";
import { useErrAPI } from "./useErrAPI";
import { TriggerApiT, UnwrappedResApiT } from "@/common/types/api";
import { __cg } from "@/core/lib/log";
import { useMsgAPI } from "./useMsgAPI";

export const useWrapAPI = () => {
  const { handleErr } = useErrAPI();

  const { handleMsgSession } = useMsgAPI();

  const wrapAPI = useCallback(
    async <T>({
      cbAPI,
      showToast = true,
      hideErr,
      throwErr,
      pushNotice,
    }: {
      cbAPI: TriggerApiT<T>;
      showToast?: boolean;
      hideErr?: boolean;
      throwErr?: boolean;
      pushNotice?: number[] | "*";
    }): Promise<UnwrappedResApiT<T> | null> => {
      try {
        const data = (await cbAPI(undefined).unwrap()) as UnwrappedResApiT<T>;

        __cg("wrapper res api", data);

        handleMsgSession({ data, showToast });

        return data;
      } catch (err: any) {
        return handleErr({ err: err, hideErr, throwErr, pushNotice });
      }
    },
    [handleErr, handleMsgSession]
  );

  return {
    wrapAPI,
  };
};
