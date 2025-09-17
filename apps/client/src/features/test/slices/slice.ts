import { createSlice } from "@reduxjs/toolkit";

const initState = {
  val: false,
};

export const testSlice = createSlice({
  name: "test",
  initialState: initState,
  reducers: {
    toggleVal: (state) => {
      state.val = !state.val;
    },
  },
});
