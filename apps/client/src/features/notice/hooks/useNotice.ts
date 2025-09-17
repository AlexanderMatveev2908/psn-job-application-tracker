import { useCallback } from "react";
import { useDispatch } from "react-redux";
import { noticeSlice, NoticeStateT } from "../slices/slice";
import { saveStorage } from "@/core/lib/storage";

export const useNotice = () => {
  const dispatch = useDispatch();

  const setNotice = useCallback(
    (noticeArg: Partial<NoticeStateT>) => {
      dispatch(noticeSlice.actions.setNotice(noticeArg));

      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { keyCb: _, ...rest } = noticeArg;

      saveStorage(rest, { key: "notice" });
    },
    [dispatch]
  );

  return {
    setNotice,
  };
};
