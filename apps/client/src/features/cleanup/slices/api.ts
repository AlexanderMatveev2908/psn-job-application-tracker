import { ResApiT } from "@/common/types/api";
import { apiSlice } from "@/core/store/api";

export const cleanupSliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    cleanCbcHmac: builder.mutation<ResApiT<void>, string>({
      query: (cbcHmacToken: string) => ({
        url: `/cleanup/cbc-hmac?cbc_hmac-token=${cbcHmacToken}`,
        method: "DELETE",
      }),
    }),
  }),
});
