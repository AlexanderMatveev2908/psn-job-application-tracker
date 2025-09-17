/** @jsxImportSource @emotion/react */
"use client";

import WrapFormPage from "@/common/components/forms/wrappers/WrapFormPage/WrapFormPage";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { useFocus } from "@/core/hooks/etc/focus/useFocus";
import { PwdFormT, pwdSchema, resetValsPwdForm } from "@/core/paperwork";
import BodyFormAccessManageAccount from "@/features/user/pages/access-manage-account/components/BodyFormAccessManageAccount";
import {
  GainAccessManageAccReturnT,
  userSliceAPI,
} from "@/features/user/slices/api";
import { zodResolver } from "@hookform/resolvers/zod";
import { type FC } from "react";
import { useForm } from "react-hook-form";
import { useUser } from "@/features/user/hooks/useUser";
import { extractAadFromCbcHmac } from "@/core/lib/dataStructure/parsers";
import { TokenT } from "@/common/types/tokens";
import { usePushOnCbcHmacPresent } from "@/features/user/hooks/usePushOnCbcHmacPresent";
import { logFormErrs } from "@/core/lib/forms";

const Page: FC = () => {
  const { wrapAPI, nav } = useKitHooks();
  const { saveCbcHmac } = useUser();
  const [mutate, { isLoading }] = userSliceAPI.useGainAccessManageAccMutation();

  const formCtx = useForm<PwdFormT>({
    mode: "onChange",
    resolver: zodResolver(pwdSchema),
    defaultValues: resetValsPwdForm,
  });

  const { handleSubmit, setFocus, reset } = formCtx;

  useFocus("password", { setFocus });

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI<GainAccessManageAccReturnT>({
      cbAPI: () => mutate(data),
    });

    if (!res) return;

    const tokenType = extractAadFromCbcHmac(res.cbc_hmac_token)?.token_t;

    saveCbcHmac(res.cbc_hmac_token);
    reset(resetValsPwdForm);

    nav.replace(
      tokenType === TokenT.MANAGE_ACC
        ? "/user/manage-account"
        : "/user/access-manage-account-2FA"
    );
  }, logFormErrs);

  usePushOnCbcHmacPresent({ tokenType: TokenT.MANAGE_ACC });

  return (
    <WrapFormPage
      {...{
        formCtx,
        formTestID: "manage_acc",
        isLoading,
        handleSave,
      }}
    >
      <BodyFormAccessManageAccount />
    </WrapFormPage>
  );
};

export default Page;
