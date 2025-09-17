/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { forwardRef } from "react";

type PropsType = {
  $twd?: string;
} & ChildrenT;

const WrapFormBody = forwardRef<HTMLDivElement, PropsType>(
  ({ children, $twd }, ref) => {
    return (
      <div ref={ref} className={`form__body__sm ${$twd ?? ""}`}>
        {children}
      </div>
    );
  }
);

WrapFormBody.displayName = "WrapFormBody";

export default WrapFormBody;
