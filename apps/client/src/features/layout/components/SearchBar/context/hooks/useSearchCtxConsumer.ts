import { useContext } from "react";
import { SearchBarCtxT } from "./useSearchCtxProvider";
import { ErrApp } from "@/core/lib/err";
import { SearchBarCtx } from "../ctx";

export const useSearchCtxConsumer = (): SearchBarCtxT => {
  const ctx = useContext<SearchBarCtxT | null>(SearchBarCtx);

  if (!ctx) throw new ErrApp("ctx must be consumed within provider 😡");

  return ctx;
};
