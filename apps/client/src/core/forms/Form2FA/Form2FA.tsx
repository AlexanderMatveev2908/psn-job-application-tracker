/** @jsxImportSource @emotion/react */
"use client";

import WrapMultiFormSwapper from "@/common/components/swap/WrapMultiFormSwapper/WrapMultiFormSwapper";
import type { FC, RefObject } from "react";
import TotpForm from "./components/TotpForm/TotpForm";
import BackupCodeForm from "./components/BackupCodeForm/BackupCodeForm";
import {
  PayloadStartSwapT,
  SwapStateT,
} from "@/core/hooks/etc/useSwap/etc/initState";
import { UseFormReturn } from "react-hook-form";
import { BackupCodeFormT, ToptFormT } from "@/core/paperwork";

export type Form2FAPropsType = {
  contentRef: RefObject<HTMLDivElement | null>;
  contentH: number;
  startSwap: (v: PayloadStartSwapT) => void;
  swapState: SwapStateT;
  totpProps: {
    formCtx: UseFormReturn<ToptFormT>;
    handleSave: () => void;
  };
  backupCodeProps: {
    formCtx: UseFormReturn<BackupCodeFormT>;
    handleSave: () => void;
  };
};

const Form2FA: FC<Form2FAPropsType> = ({
  contentH,
  contentRef,
  startSwap,
  swapState,
  backupCodeProps,
  totpProps,
}) => {
  return (
    <WrapMultiFormSwapper
      {...{
        formTestID: "2FA",
        propsBtnsSwapper: {
          startSwap,
        },
        propsWrapSwapper: {
          contentH,
        },
        swapState,
        totSwaps: 2,
      }}
    >
      <TotpForm
        {...{
          contentRef,
          isCurr: !swapState.currSwap,
          formCtx: totpProps.formCtx,
          handleSave: totpProps.handleSave,
          swapState,
        }}
      />

      <BackupCodeForm
        {...{
          contentRef,
          isCurr: !!swapState.currSwap,
          formCtx: backupCodeProps.formCtx,
          handleSave: backupCodeProps.handleSave,
          swapState,
        }}
      />
    </WrapMultiFormSwapper>
  );
};

export default Form2FA;
