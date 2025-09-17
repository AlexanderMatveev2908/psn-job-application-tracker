import { useDispatch } from "react-redux";
import { userSlice } from "../slices/slice";
import { useCallback } from "react";
import { clearStorage, delStorageItm, saveStorage } from "@/core/lib/storage";
import { useGetUserState } from "./useGetUserState";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { authSliceAPI } from "@/features/auth/slices/api";
import { useRouter } from "next/navigation";
import { apiSlice } from "@/core/store/api";
import { instanceAxs } from "@/core/store/conf/baseQuery/axiosInstance";

export const useUser = () => {
  const { wrapAPI } = useWrapAPI();

  const nav = useRouter();

  const dispatch = useDispatch();
  const [mutate, { isLoading: isVoluntaryLoggingOut }] =
    authSliceAPI.useLogoutAuthMutation();

  const loginUser = useCallback(
    (access_token: string) => {
      saveStorage(access_token, { key: "access_token" });
      dispatch(userSlice.actions.login({ access_token }));
    },
    [dispatch]
  );

  const commonLogoutActions = useCallback(() => {
    clearStorage();
    delete instanceAxs.defaults.headers.common.Authorization;
    dispatch(userSlice.actions.logout());
    dispatch(apiSlice.util.resetApiState());
  }, [dispatch]);

  const voluntaryLogoutUser = useCallback(async () => {
    const res = await wrapAPI({
      cbAPI: () => mutate(),
    });

    if (!res) return;

    commonLogoutActions();

    nav.replace("/");
  }, [wrapAPI, mutate, commonLogoutActions, nav]);

  const saveCbcHmac = useCallback(
    (cbc_hmac_token: string) => {
      dispatch(userSlice.actions.setCbcHmac(cbc_hmac_token));
      saveStorage(cbc_hmac_token, { key: "cbc_hmac_token" });
    },
    [dispatch]
  );

  const delCbcHmac = useCallback(() => {
    delStorageItm("cbc_hmac_token");
    dispatch(userSlice.actions.clearCbcHmac());
  }, [dispatch]);

  return {
    userState: useGetUserState(),
    loginUser,
    voluntaryLogoutUser,
    isVoluntaryLoggingOut,
    commonLogoutActions,
    saveCbcHmac,
    delCbcHmac,
  };
};
