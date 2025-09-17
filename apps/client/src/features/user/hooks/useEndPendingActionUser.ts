import { useDispatch } from "react-redux";
import { useGetUserState } from "./useGetUserState";
import { useEffect, useRef } from "react";
import { userSlice } from "../slices/slice";
import { clearTmr } from "@/core/lib/etc";

export const useEndPendingActionUser = () => {
  const usState = useGetUserState();
  const dispatch = useDispatch();

  const actionSessionTimerID = useRef<NodeJS.Timeout | null>(null);
  const actionCbcHmacTimerID = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    if (usState.pendingActionSession)
      actionSessionTimerID.current = setTimeout(() => {
        dispatch(userSlice.actions.endPendingActionSession());
        clearTmr(actionSessionTimerID);
      }, 2500);

    return () => {
      clearTmr(actionSessionTimerID);
    };
  }, [usState.pendingActionSession, dispatch]);

  useEffect(() => {
    if (usState.pendingActionCbcHmac)
      actionCbcHmacTimerID.current = setTimeout(() => {
        dispatch(userSlice.actions.endPendingActionCbcHmac());
        clearTmr(actionCbcHmacTimerID);
      }, 2500);

    return () => {
      clearTmr(actionCbcHmacTimerID);
    };
  }, [usState.pendingActionCbcHmac, dispatch]);
};
