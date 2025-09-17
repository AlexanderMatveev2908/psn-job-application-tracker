/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { css } from "@emotion/react";
import type { FC } from "react";

const FilterBarBodyWrapCol: FC<ChildrenT> = ({ children }) => {
  return (
    <div
      className="w-full flex flex-col min-h-0 p-3 overflow-y-auto scroll__app items-start gap-6"
      // ? remove from 100% the header(60px) and the footer(80px)
      css={css`
        max-height: calc(100% - ${60 + 80}px);
      `}
    >
      {children}
    </div>
  );
};

export default FilterBarBodyWrapCol;
