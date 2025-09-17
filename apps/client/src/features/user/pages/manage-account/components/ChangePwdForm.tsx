/** @jsxImportSource @emotion/react */
"use client";

import { type FC } from "react";
import { FormManageAccPropsType } from "../types";
import { useForm } from "react-hook-form";
import { PwdsFormT, pwdsSchema, resetValsPwdsForm } from "@/core/paperwork";
import { zodResolver } from "@hookform/resolvers/zod";
import { useFocusMultiForm } from "@/core/hooks/etc/focus/useFocusMultiForm";
import { userSliceAPI } from "@/features/user/slices/api";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import PairPwd from "@/common/components/HOC/PairPwd/PairPwd";
import WrapSwapMultiForm from "@/common/components/swap/WrapMultiFormSwapper/subComponents/WrapSwapMultiForm";
import { logFormErrs } from "@/core/lib/forms";

const ChangePwdForm: FC<FormManageAccPropsType> = ({
  contentRef,
  isCurr,
  swapState,
}) => {
  const { cbc_hmac_token } = useGetUserState();
  const { wrapAPI } = useKitHooks();
  const [mutate, { isLoading }] = userSliceAPI.useChangePwdUserMutation();

  const formCtx = useForm<PwdsFormT>({
    mode: "onChange",
    resolver: zodResolver(pwdsSchema),
    defaultValues: resetValsPwdsForm,
  });
  const { handleSubmit, setFocus, reset } = formCtx;

  useFocusMultiForm({
    keyField: "password",
    setFocus,
    swapState,
    targetSwap: 1,
  });

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI({
      cbAPI: () => mutate({ ...data, cbc_hmac_token }),
      pushNotice: [401],
    });

    if (!res) return;

    reset(resetValsPwdsForm);
  }, logFormErrs);

  return (
    <WrapSwapMultiForm
      {...{
        contentRef,
        isCurr,
        title: "Change Password",
        handleSave,
        formCtx,
        isLoading,
      }}
    >
      <PairPwd
        {...{
          isCurrSwap: isCurr,
          swapMode: swapState.swapMode,
        }}
      />
    </WrapSwapMultiForm>
  );
};

export default ChangePwdForm;
