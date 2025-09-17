/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { type FC } from "react";
import { useSearchCtxProvider } from "./hooks/useSearchCtxProvider";
import { SearchBarCtx } from "./ctx";

const SearchBarCtxProvider: FC<ChildrenT> = ({ children }) => {
  return (
    <SearchBarCtx.Provider value={useSearchCtxProvider()}>
      {children}
    </SearchBarCtx.Provider>
  );
};

export default SearchBarCtxProvider;
