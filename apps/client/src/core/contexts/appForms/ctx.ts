import { createContext } from "react";
import { AppFormsCtxT } from "./hooks/useAppFormsCtxProvider";

export const AppFormsCtx = createContext<AppFormsCtxT | null>(null);
