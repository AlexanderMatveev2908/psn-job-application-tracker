// hooks/useToastStages.ts
import { useCallback, useEffect, useRef } from "react";
import { toastSlice, ToastStateT } from "../slices";
import { DispatchT } from "@/core/store";
import { clearTmr } from "@/core/lib/etc";

export const useToastStages = ({
  dispatch,
  toastState,
}: {
  dispatch: DispatchT;
  toastState: ToastStateT;
}) => {
  const timerID = useRef<ReturnType<typeof setTimeout> | null>(null);

  const clickClose = useCallback(() => {
    clearTmr(timerID);
    dispatch(toastSlice.actions.close());
  }, [dispatch]);

  useEffect(() => {
    if (!toastState.isShow) return;
    clearTmr(timerID);

    timerID.current = setTimeout(() => {
      dispatch(toastSlice.actions.close());
    }, 5000);

    return () => {
      clearTmr(timerID);
    };
  }, [toastState.isShow, toastState.x, dispatch]);

  return { clickClose };
};
