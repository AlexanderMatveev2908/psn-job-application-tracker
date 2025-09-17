import { ResApiT } from "@/common/types/api";
import { isArrOk, isStr } from "@/core/lib/dataStructure/ect";
import { ErrApp } from "@/core/lib/err";
import { __cg } from "@/core/lib/log";
import { apiSlice } from "@/core/store/api";
import { toastSlice } from "@/features/layout/components/Toast/slices";
import { useNotice } from "@/features/notice/hooks/useNotice";
import { useRouter } from "next/navigation";
import { useCallback } from "react";
import { useDispatch } from "react-redux";

export const useErrAPI = () => {
  const dispatch = useDispatch();

  const nav = useRouter();

  const { setNotice } = useNotice();

  const handleErr = useCallback(
    <T>({
      err,
      hideErr,
      throwErr,
      pushNotice,
    }: {
      err: ResApiT<T>["data"];
      hideErr?: boolean;
      throwErr?: boolean;
      pushNotice?: number[] | "*";
    }): null => {
      __cg("wrapper err api", err);

      if (err?.refreshFailed) {
        dispatch(
          toastSlice.actions.open({
            msg: err?.msg ?? "session expired",
            type: "ERR",
          })
        );

        dispatch(apiSlice.util.resetApiState());

        nav.replace("/auth/login");
      } else {
        if (throwErr && hideErr) throw new ErrApp("Logic Conflict 😡");

        if (!hideErr) {
          const sureMsgExists = isStr(err?.msg)
            ? err.msg!
            : "Ops something went wrong ❌";

          dispatch(
            toastSlice.actions.open({
              msg: sureMsgExists,
              type: "ERR",
            })
          );

          const argCheckStatus = [
            429,
            ...(isArrOk(pushNotice, (v) => typeof v === "number")
              ? (pushNotice as number[])
              : []),
          ];

          if (
            argCheckStatus.includes(err?.status ?? 500) ||
            pushNotice === "*"
          ) {
            setNotice({
              type: "ERR",
              msg: sureMsgExists,
            });

            nav.replace("/notice");
          }
        }
      }

      if (throwErr) throw err;

      return null;
    },
    [dispatch, nav, setNotice]
  );

  return {
    handleErr,
  };
};
