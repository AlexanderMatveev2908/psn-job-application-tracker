/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { FieldValues, FormProvider, UseFormReturn } from "react-hook-form";
import WrapFormFooter from "./subComponents/WrapFormFooter";
import { Fragment, ReactNode } from "react";
import { PropsTypeBtnsSwapper } from "../../../swap/components/BtnsSwapper";
import ProgressSwap from "../../../swap/components/ProgressSwap";
import WrapCSR from "../../../wrappers/pages/WrapCSR";
import WrapFormBody from "./subComponents/WrapFormBody";

export type WrapFormPagePropsType<T extends FieldValues> = {
  propsProgressSwap?: {
    currSwap: number;
    totSwaps: number;
  };
  propsBtnsSwapper?: PropsTypeBtnsSwapper;
  formCtx: UseFormReturn<T>;
  handleSave: () => void;
  formTestID: string;
  isLoading: boolean;
  AdditionalFooterNode?: () => ReactNode;
} & ChildrenT;

const WrapFormPage = <T extends FieldValues>({
  propsProgressSwap,
  children,
  formCtx,
  handleSave,
  formTestID,
  isLoading,
  propsBtnsSwapper,
  AdditionalFooterNode,
}: WrapFormPagePropsType<T>) => {
  const ChildrenWrapper = propsBtnsSwapper ? Fragment : WrapFormBody;

  return (
    <WrapCSR>
      {propsProgressSwap && (
        <ProgressSwap
          {...{
            maxW: 800,
            ...propsProgressSwap,
          }}
        />
      )}

      <FormProvider {...formCtx}>
        <form
          data-testid={formTestID + "__form"}
          className="form__shape__sm"
          onSubmit={handleSave}
        >
          <ChildrenWrapper>{children}</ChildrenWrapper>

          <WrapFormFooter
            {...{
              propsBtnsSwapper,
              isLoading,
              submitBtnTestID: formTestID,
            }}
          />

          {typeof AdditionalFooterNode === "function" && AdditionalFooterNode()}
        </form>
      </FormProvider>
    </WrapCSR>
  );
};

export default WrapFormPage;
