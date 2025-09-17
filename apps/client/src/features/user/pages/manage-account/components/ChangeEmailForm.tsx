/** @jsxImportSource @emotion/react */
"use client";

import FormFieldTxt from "@/common/components/forms/inputs/FormFieldTxt";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { EmailFormT, emailSchema, resetValsEmailForm } from "@/core/paperwork";
import { emailField } from "@/core/uiFactory/formFields";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { userSliceAPI } from "@/features/user/slices/api";
import { zodResolver } from "@hookform/resolvers/zod";
import { type FC } from "react";
import { useForm } from "react-hook-form";
import { FormManageAccPropsType } from "../types";
import { useFocusMultiForm } from "@/core/hooks/etc/focus/useFocusMultiForm";
import WrapSwapMultiForm from "@/common/components/swap/WrapMultiFormSwapper/subComponents/WrapSwapMultiForm";
import { logFormErrs } from "@/core/lib/forms";

const ChangeEmailForm: FC<FormManageAccPropsType> = ({
  contentRef,
  isCurr,
  swapState,
}) => {
  const { user } = useGetUserState();
  const { currSwap, swapMode } = swapState;

  const schemaX = emailSchema.refine((data) => data.email !== user?.email, {
    message: "new email must be different from old one",
    path: ["email"],
  });

  const formCtx = useForm<EmailFormT>({
    mode: "onChange",
    resolver: zodResolver(schemaX),
    defaultValues: resetValsEmailForm,
  });
  const { handleSubmit, setFocus, reset } = formCtx;

  const [mutate, { isLoading }] = userSliceAPI.useChangeEmailMutation();
  const { cbc_hmac_token } = useGetUserState();
  const { setNotice, wrapAPI, nav } = useKitHooks();

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI({
      cbAPI: () =>
        mutate({
          ...data,
          cbc_hmac_token,
        }),
      pushNotice: [401],
    });

    if (!res) return;

    setNotice({
      msg: genMailNoticeMsg("to change your email address"),
      type: "OK",
      child: "OPEN_MAIL_APP",
    });

    reset(resetValsEmailForm);

    nav.replace("/notice");
  }, logFormErrs);

  const { control } = formCtx;

  useFocusMultiForm({
    keyField: "email",
    setFocus,
    swapState,
    targetSwap: 0,
  });

  return (
    <WrapSwapMultiForm
      {...{
        contentRef,
        isCurr,
        title: "Change Email",
        handleSave,
        formCtx,
        isLoading,
      }}
    >
      <FormFieldTxt
        {...{
          el: emailField,
          control,
          portalConf: {
            showPortal: isCurr && swapMode !== "swapping",
            optDep: [currSwap, swapMode],
          },
        }}
      />
    </WrapSwapMultiForm>
  );
};

export default ChangeEmailForm;
