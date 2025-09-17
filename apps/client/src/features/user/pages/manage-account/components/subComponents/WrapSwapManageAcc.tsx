/** @jsxImportSource @emotion/react */
"use client";

import Title from "@/common/components/elements/txt/Title";
import WrapSwap, {
  PropsTypeWrapSwap,
} from "@/common/components/swap/components/WrapSwap";
import { ChildrenT } from "@/common/types/ui";
import type { FC } from "react";

type PropsType = {
  title: string;
  testID: string;
} & ChildrenT &
  Omit<PropsTypeWrapSwap, "children">;

const WrapSwapManageAcc: FC<PropsType> = ({
  children,
  contentRef,
  isCurr,
  title,
  testID,
}) => {
  return (
    <WrapSwap
      {...{
        contentRef,
        isCurr,
      }}
    >
      <div data-testid={testID + "__swap"} className="cont__grid__lg py-5">
        <Title
          {...{
            title,
            $twdCls: "2xl",
          }}
        />
        {children}
      </div>
    </WrapSwap>
  );
};

export default WrapSwapManageAcc;
