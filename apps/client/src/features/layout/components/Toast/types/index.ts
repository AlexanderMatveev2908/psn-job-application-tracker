import { AppEventT } from "@/common/types/api";

export type ToastT = {
  msg: string;
  type: AppEventT;
};
export type ToastStateT = {
  isShow: boolean;
  toast: ToastT;
  id: string;
};
