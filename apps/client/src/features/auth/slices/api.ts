import { JwtReturnT, ResApiT, TagAPI } from "@/common/types/api";
import { apiSlice } from "@/core/store/api";
import { RegisterFormT } from "../pages/register/paperwork";
import { LoginFormT } from "../pages/login/paperwork";
import { ParamsAPI2FA } from "@/core/paperwork";

const BASE = "/auth";

export type LoginUserReturnT = {
  access_token?: string;
  cbc_hmac_token?: string;
};

export const authSliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    registerAuth: builder.mutation<ResApiT<JwtReturnT>, RegisterFormT>({
      query: (data) => ({
        url: `${BASE}/register`,
        method: "POST",
        data,
      }),
      invalidatesTags: [TagAPI.USER],
    }),

    loginAuth: builder.mutation<ResApiT<JwtReturnT>, LoginFormT>({
      query: (data) => ({
        url: `${BASE}/login`,
        method: "POST",
        data,
      }),
      invalidatesTags: [TagAPI.USER],
    }),

    logoutAuth: builder.mutation<ResApiT<void>, void>({
      query: () => ({
        url: `${BASE}/logout`,
        method: "POST",
      }),
    }),

    recoverPwdAuth: builder.mutation<
      ResApiT<JwtReturnT>,
      { cbc_hmac_token: string; password: string }
    >({
      query: (data) => ({
        url: `${BASE}/recover-pwd`,
        method: "PATCH",
        data,
      }),
      invalidatesTags: [TagAPI.USER],
    }),

    loginAuth2FA: builder.mutation<ResApiT<JwtReturnT>, ParamsAPI2FA>({
      query: (data) => ({
        url: `${BASE}/login-2FA`,
        method: "POST",
        data,
      }),
      invalidatesTags: [TagAPI.USER],
    }),

    recoverPwdAuthReset2FA: builder.mutation<
      ResApiT<JwtReturnT>,
      { cbc_hmac_token: string; password: string }
    >({
      query: (data) => ({
        url: `${BASE}/recover-pwd-2FA`,
        method: "PATCH",
        data,
      }),

      invalidatesTags: [TagAPI.USER],
    }),
  }),
});
