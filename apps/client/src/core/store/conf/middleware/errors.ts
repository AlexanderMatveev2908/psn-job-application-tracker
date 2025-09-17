/* eslint-disable @typescript-eslint/no-explicit-any */
import { Dispatch, UnknownAction } from "@reduxjs/toolkit";
import { REG_JWT } from "@/core/constants/regex";
import { userSlice } from "@/features/user/slices/slice";
import { __cg } from "@/core/lib/log";
import { StoreStateT } from "../..";

export const handleErrorsActions =
  (store: any) => (next: Dispatch<any>) => (action: UnknownAction) => {
    const { payload } = (action ?? {}) as any;

    try {
      const pendingActionSession = store.getState().user.pendingActionSession;

      if (
        pendingActionSession ||
        (!payload?.refreshFailed && !payload?.refreshed)
      )
        return next(action);

      const isLogged = REG_JWT.test(
        (store.getState() as StoreStateT).user.access_token
      );

      if (payload?.refreshed && !isLogged) {
        store.dispatch(
          userSlice.actions.login({ access_token: payload.access_token })
        );
        payload.restoredSession = true;
      } else if (payload?.refreshFailed && isLogged) {
        store.dispatch(userSlice.actions.logout());
      }
    } catch (err: any) {
      __cg("err mdw", err);

      return next(action);
    }

    return next(action);
  };
