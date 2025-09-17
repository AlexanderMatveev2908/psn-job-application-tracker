/** @jsxImportSource @emotion/react */
"use client";

import FormFieldPwd from "@/common/components/forms/inputs/FormFieldPwd";
import { PropsTypeWrapSwap } from "@/common/components/swap/components/WrapSwap";
import WrapSwapMultiForm from "@/common/components/swap/WrapMultiFormSwapper/subComponents/WrapSwapMultiForm";
import { useFocusMultiForm } from "@/core/hooks/etc/focus/useFocusMultiForm";
import { SwapStateT } from "@/core/hooks/etc/useSwap/etc/initState";
import { useTogglePwd } from "@/core/hooks/etc/useTogglePwd";
import { BackupCodeFormT } from "@/core/paperwork";
import { FormFieldGen } from "@/core/uiFactory/classes";
import type { FC } from "react";
import { UseFormReturn } from "react-hook-form";

type PropsType = {
  formCtx: UseFormReturn<BackupCodeFormT>;
  swapState: SwapStateT;
  handleSave: () => void;
} & Omit<PropsTypeWrapSwap, "children">;

const BackupCodeForm: FC<PropsType> = ({
  formCtx,
  handleSave,
  contentRef,
  isCurr,
  swapState,
}) => {
  const { control, setFocus } = formCtx;

  const { handlePwdClick, isPwdShw } = useTogglePwd();

  useFocusMultiForm<BackupCodeFormT>({
    keyField: "backup_code",
    setFocus,
    swapState,
    targetSwap: 1,
  });

  return (
    <WrapSwapMultiForm
      {...{
        contentRef,
        isCurr,
        formCtx,
        handleSave,
        isLoading: formCtx.formState.isSubmitting,
        title: "Backup Code",
      }}
    >
      <FormFieldPwd
        {...{
          control,
          el: new FormFieldGen<BackupCodeFormT>().txtField({
            name: "backup_code",
          }),
          isPwdShw,
          handleSvgClick: handlePwdClick,
        }}
      />
    </WrapSwapMultiForm>
  );
};

export default BackupCodeForm;
