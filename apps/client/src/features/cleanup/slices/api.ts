import { ResApiT } from "@/common/types/api";
import { apiSlice } from "@/core/store/api";

export const cleanupSliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    cleanCbcHmac: builder.mutation<ResApiT<void>, string>({
      query: (cbc_hmac_token: string) => ({
        url: `/cleanup/cbc-hmac?cbc_hmac-token=${cbc_hmac_token}`,
        method: "DELETE",
      }),
    }),
  }),
});
