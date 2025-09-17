import { createApi } from "@reduxjs/toolkit/query/react";
import { baseQueryAxs } from "./conf/baseQuery/baseQuery";
import { TagAPI } from "@/common/types/api";

export const apiSlice = createApi({
  baseQuery: baseQueryAxs,
  tagTypes: Object.values(TagAPI),
  reducerPath: "apiApp",
  endpoints: () => ({}),
});
