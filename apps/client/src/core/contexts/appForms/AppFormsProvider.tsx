/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import type { FC } from "react";
import { AppFormsCtx } from "./ctx";
import { useAppFormsCtxProvider } from "./hooks/useAppFormsCtxProvider";

const AppFormsProvider: FC<ChildrenT> = ({ children }) => {
  return (
    <AppFormsCtx.Provider value={useAppFormsCtxProvider()}>
      {children}
    </AppFormsCtx.Provider>
  );
};

export default AppFormsProvider;
