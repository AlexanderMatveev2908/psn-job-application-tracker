import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { UserT } from "../types";
import { StoreStateT } from "@/core/store";

export interface UserStateT {
  // ? pending-action is specific for moments like logging in or logging out / being pushed out , where i need a reference to avoid being interrupted by existing custom route blocker that protect pages but that are generic while exists specific cases must be handled manually
  pendingActionSession: boolean;
  pendingActionCbcHmac: boolean;
  touchedServer: boolean;
  accessToken: string;
  cbcHmacToken: string;
  user: UserT | null;
}

const initState: UserStateT = {
  pendingActionSession: false,
  pendingActionCbcHmac: false,
  accessToken: "",
  cbcHmacToken: "",
  user: null,
  touchedServer: false,
};

export const userSlice = createSlice({
  name: "user",
  initialState: initState,
  reducers: {
    login: (state, action: PayloadAction<{ accessToken: string }>) => {
      state.pendingActionSession = true;
      state.accessToken = action.payload.accessToken;
    },

    setAccessToken: (state, action: PayloadAction<{ accessToken: string }>) => {
      state.accessToken = action.payload.accessToken;
    },

    setUser: (state, action: PayloadAction<UserT | undefined | null>) => {
      state.user = action.payload ?? null;
      state.touchedServer = true;
    },

    logout: () => ({ ...initState, pendingActionSession: true }),

    setCbcHmac: (state, action: PayloadAction<string>) => {
      state.cbcHmacToken = action.payload;
      state.pendingActionCbcHmac = true;
    },
    clearCbcHmac: (state) => {
      state.cbcHmacToken = "";
    },

    endPendingActionSession: (state) => {
      state.pendingActionSession = false;
    },
    endPendingActionCbcHmac: (state) => {
      state.pendingActionCbcHmac = false;
    },
  },
});

export const getUserState = (state: StoreStateT) => state.user;
