import { useContext } from "react";
import { AppFormsCtx } from "../ctx";
import { ErrApp } from "@/core/lib/err";

export const useAppFormsCtxConsumer = () => {
  const ctx = useContext(AppFormsCtx);

  if (!ctx) throw new ErrApp("ctx must consumed within a provider 😡");

  return ctx;
};
