import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { AppEventT } from "@/common/types/api";
import { StoreStateT } from "@/core/store";

export type KeyCbT = "LOGIN";
export type ChildNoticeT = "OPEN_MAIL_APP";

export interface NoticeStateT {
  type: AppEventT;
  msg: string;
  keyCb: KeyCbT;
  child: ChildNoticeT;
}

export const noticeInitState: NoticeStateT = {
  type: "NONE",
  msg: "",
  keyCb: "" as KeyCbT,
  child: "" as ChildNoticeT,
};

export const noticeSlice = createSlice({
  name: "notice",
  initialState: noticeInitState,
  reducers: {
    setNotice: (state, action: PayloadAction<Partial<NoticeStateT>>) => {
      const { keyCb, msg, type, child } = action.payload;

      state.msg = msg ?? "";
      state.type = type ?? "NONE";
      state.keyCb = keyCb ?? ("" as KeyCbT);
      state.child = child ?? ("" as ChildNoticeT);
    },
  },
});

export const getNoticeState = (state: StoreStateT) => state.notice;
