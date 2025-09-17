/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { UseFormReturn } from "react-hook-form";
import WrapFormPage from "@/common/components/forms/wrappers/WrapFormPage/WrapFormPage";
import RequireEmailFormBody from "./RequireEmailFormBody";
import { EmailFormT } from "@/core/paperwork";

export type PropsTypeRequireEmailForm = {
  formCtx: UseFormReturn<EmailFormT>;
  handleSave: () => void;
  isLoading: boolean;
  testID: string;
};

const RequireEmailForm: FC<PropsTypeRequireEmailForm> = ({
  formCtx,
  handleSave,
  isLoading,
  testID,
}) => {
  return (
    <WrapFormPage
      {...{
        formCtx,
        handleSave,
        formTestID: testID,
        isLoading,
      }}
    >
      <RequireEmailFormBody
        {...{
          formCtx,
        }}
      />
    </WrapFormPage>
  );
};

export default RequireEmailForm;
