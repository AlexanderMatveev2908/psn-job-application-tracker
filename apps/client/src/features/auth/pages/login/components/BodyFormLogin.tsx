/** @jsxImportSource @emotion/react */
"use client";

import FormFieldTxt from "@/common/components/forms/inputs/FormFieldTxt";
import type { FC } from "react";
import { useFormContext } from "react-hook-form";
import { LoginFormT } from "../paperwork";
import { useTogglePwd } from "@/core/hooks/etc/useTogglePwd";
import FormFieldPwd from "@/common/components/forms/inputs/FormFieldPwd";
import { emailField, pwdField } from "@/core/uiFactory/formFields";
import { FormFieldTxtT } from "@/common/types/ui";

const BodyFormLogin: FC = () => {
  const { control } = useFormContext<LoginFormT>();

  const { handlePwdClick, isPwdShw } = useTogglePwd();

  return (
    <>
      <FormFieldTxt
        {...{
          el: emailField as unknown as FormFieldTxtT<LoginFormT>,
          control,
        }}
      />

      <FormFieldPwd
        {...{
          el: pwdField as unknown as FormFieldTxtT<LoginFormT>,
          control,
          isPwdShw: isPwdShw,
          handleSvgClick: handlePwdClick,
        }}
      />
    </>
  );
};

export default BodyFormLogin;
