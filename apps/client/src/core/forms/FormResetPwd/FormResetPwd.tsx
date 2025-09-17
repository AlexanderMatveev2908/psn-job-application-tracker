/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { UseFormReturn } from "react-hook-form";
import WrapAuthFormPage from "@/features/auth/components/WrapAuthFormPage";
import { PwdsFormT } from "@/core/paperwork";
import PairPwd from "@/common/components/HOC/PairPwd/PairPwd";

type PropsType = {
  formCtx: UseFormReturn<PwdsFormT>;
  handleSave: () => void;
  testID: string;
  isLoading: boolean;
};

const FormResetPwd: FC<PropsType> = ({
  formCtx,
  handleSave,
  testID,
  isLoading,
}) => {
  return (
    <WrapAuthFormPage
      {...{
        formCtx,
        handleSave,
        formTestID: testID,
        isLoading,
      }}
    >
      <PairPwd />
    </WrapAuthFormPage>
  );
};

export default FormResetPwd;
