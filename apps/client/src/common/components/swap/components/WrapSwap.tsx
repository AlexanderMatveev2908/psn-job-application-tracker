/** @jsxImportSource @emotion/react */
"use client";

import WrapFormBody from "@/common/components/forms/wrappers/WrapFormPage/subComponents/WrapFormBody";
import { ChildrenT } from "@/common/types/ui";
import type { FC, RefObject } from "react";

export type PropsTypeWrapSwap = {
  isCurr: boolean;
  contentRef: RefObject<HTMLDivElement | null>;
} & ChildrenT;

const WrapSwap: FC<PropsTypeWrapSwap> = ({ children, isCurr, contentRef }) => {
  return (
    <WrapFormBody
      ref={isCurr ? contentRef : null}
      {...{
        $twd: `transition-all duration-300 ${
          isCurr
            ? "opacity-100 pointer-events-auto"
            : "opacity-0 pointer-events-none"
        }`,
      }}
    >
      {children}
    </WrapFormBody>
  );
};

export default WrapSwap;
