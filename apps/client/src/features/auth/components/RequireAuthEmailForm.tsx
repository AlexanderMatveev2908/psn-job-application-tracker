/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { PropsTypeRequireEmailForm } from "../../../core/forms/RequireEmailForm/RequireEmailForm";
import WrapAuthFormPage from "@/features/auth/components/WrapAuthFormPage";
import RequireEmailFormBody from "../../../core/forms/RequireEmailForm/RequireEmailFormBody";

const RequireAuthEmailForm: FC<PropsTypeRequireEmailForm> = ({
  formCtx,
  handleSave,
  isLoading,
  testID,
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
      <RequireEmailFormBody
        {...{
          formCtx,
        }}
      />
    </WrapAuthFormPage>
  );
};

export default RequireAuthEmailForm;
