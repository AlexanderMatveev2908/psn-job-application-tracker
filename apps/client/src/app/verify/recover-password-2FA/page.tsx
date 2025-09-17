/** @jsxImportSource @emotion/react */
"use client";

import { CbcHmacReturnT, UnwrappedResApiT } from "@/common/types/api";
import { TokenT } from "@/common/types/tokens";
import Form2FA from "@/core/forms/Form2FA/Form2FA";
import { use2FAForm } from "@/core/hooks/etc/forms/use2FAForm";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { useUser } from "@/features/user/hooks/useUser";
import { verifySliceAPI } from "@/features/verify/slices/api";

import { useCallback, type FC } from "react";

const Page: FC = () => {
  const [mutate] = verifySliceAPI.useRecoverPwd2FAMutation();
  const { nav } = useKitHooks();
  const { saveCbcHmac } = useUser();

  const successCb = useCallback(
    async (res: UnwrappedResApiT<CbcHmacReturnT>) => {
      saveCbcHmac(res.cbc_hmac_token);

      nav.replace("/auth/recover-password-2FA");
    },
    [nav, saveCbcHmac]
  );

  const props = use2FAForm({
    mutationTrigger: mutate,
    successCb,
    delCbcOnSuccess: false,
    tokenType: TokenT.RECOVER_PWD,
  });

  return <Form2FA {...{ ...props }} />;
};

export default Page;
