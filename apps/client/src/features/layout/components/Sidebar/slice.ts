import { StoreStateT } from "@/core/store";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initState = {
  isOpen: false,
};

export const sideSlice = createSlice({
  name: "side",
  initialState: initState,
  reducers: {
    toggleSide: (state, action: PayloadAction<boolean | undefined>) => {
      state.isOpen = typeof action === "boolean" ? action : !state.isOpen;
    },

    closeSide: (state) => {
      state.isOpen = false;
    },
  },
});

export const getSideState = (state: StoreStateT) => state.side;
