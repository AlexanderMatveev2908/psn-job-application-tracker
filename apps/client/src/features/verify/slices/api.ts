import {
  CbcHmacReturnT,
  JwtReturnT,
  ResApiT,
  TagAPI,
} from "@/common/types/api";
import { ParamsAPI2FA } from "@/core/paperwork";
import { apiSlice } from "@/core/store/api";

const BASE = "/verify";

export type VerifyCbcHmacReturnT = {
  cbcHmacToken?: string;
  accessToken?: string;
  strategy2FA?: boolean;
};

export type VerifyCbcHmacEndpointT =
  | "confirm-email"
  | "recover-pwd"
  | "new-email";

export type VerifyCbcHmacArgT = {
  endpoint: VerifyCbcHmacEndpointT;
  cbcHmacToken: string;
};

export const verifySliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    verifyCbcHmac: builder.query<
      ResApiT<VerifyCbcHmacReturnT>,
      VerifyCbcHmacArgT
    >({
      query: (data) => ({
        url: `${BASE}/${data.endpoint}?cbcHmacToken=${data.cbcHmacToken}`,
        method: "GET",
      }),
    }),

    changeEmail2FA: builder.mutation<ResApiT<JwtReturnT>, ParamsAPI2FA>({
      query: (data) => ({
        url: `${BASE}/new-email-2FA`,
        method: "PATCH",
        data,
      }),

      invalidatesTags: [TagAPI.USER],
    }),

    recoverPwd2FA: builder.mutation<ResApiT<CbcHmacReturnT>, ParamsAPI2FA>({
      query: (data) => ({
        url: `${BASE}/recover-pwd-2FA`,
        method: "POST",
        data,
      }),
    }),
  }),
});
