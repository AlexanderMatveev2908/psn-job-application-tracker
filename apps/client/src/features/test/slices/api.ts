import { ResApiT } from "@/common/types/api";
import { apiSlice } from "@/core/store/api";

const BASE_URL = "/test/";

export const testSliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getHello: builder.query<ResApiT<void>, void>({
      query: () => ({
        url: BASE_URL,
        method: "GET",
      }),
    }),

    postHello: builder.mutation<
      ResApiT<void>,
      {
        msg: string;
      }
    >({
      query: ({ msg }) => ({
        url: BASE_URL,
        method: "POST",
        data: { msg },
      }),
    }),

    getProtected: builder.query<ResApiT<void>, void>({
      query: () => ({
        url: `${BASE_URL}protected`,
        method: "GET",
      }),
    }),
  }),
});
