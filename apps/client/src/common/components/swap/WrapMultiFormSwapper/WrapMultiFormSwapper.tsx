/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { ChildrenT } from "@/common/types/ui";
import {
  PayloadStartSwapT,
  SwapStateT,
} from "@/core/hooks/etc/useSwap/etc/initState";
import WrapSwapper from "../WrapSwapper";
import WrapFormFooter from "../../forms/wrappers/WrapFormPage/subComponents/WrapFormFooter";
import WrapCSR from "../../wrappers/pages/WrapCSR";

type PropsType = {
  formTestID: string;
  propsBtnsSwapper: {
    startSwap: (v: PayloadStartSwapT) => void;
  };
  propsWrapSwapper: {
    contentH: number;
  };
  totSwaps: number;
  swapState: SwapStateT;
} & ChildrenT;

const WrapMultiFormSwapper: FC<PropsType> = ({
  children,
  formTestID,
  propsBtnsSwapper,
  propsWrapSwapper,
  totSwaps,
  swapState,
}) => {
  return (
    <WrapCSR>
      <div data-testid={formTestID + "__form"} className="form__shape__sm">
        <WrapSwapper
          {...{
            ...propsWrapSwapper,
            totSwaps,
            currSwap: swapState.currSwap,
          }}
        >
          {children}
        </WrapSwapper>

        <WrapFormFooter
          {...{
            propsBtnsSwapper: {
              ...propsBtnsSwapper,
              currSwap: swapState.currSwap,
              totSwaps,
            },
          }}
        />
      </div>
    </WrapCSR>
  );
};

export default WrapMultiFormSwapper;
