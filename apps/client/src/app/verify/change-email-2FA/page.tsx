/** @jsxImportSource @emotion/react */
"use client";

import { JwtReturnT, UnwrappedResApiT } from "@/common/types/api";
import { TokenT } from "@/common/types/tokens";
import Form2FA from "@/core/forms/Form2FA/Form2FA";
import { use2FAForm } from "@/core/hooks/etc/forms/use2FAForm";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { useUser } from "@/features/user/hooks/useUser";
import { verifySliceAPI } from "@/features/verify/slices/api";
import { useCallback, type FC } from "react";

const Page: FC = () => {
  const [mutate] = verifySliceAPI.useChangeEmail2FAMutation();
  const { nav } = useKitHooks();
  const { loginUser } = useUser();

  const successCb = useCallback(
    async (res: UnwrappedResApiT<JwtReturnT>) => {
      loginUser(res.access_token);
      nav.replace("/");
    },
    [loginUser, nav]
  );

  const props = use2FAForm({
    mutationTrigger: mutate,
    successCb,
    delCbcOnSuccess: true,
    tokenType: TokenT.CHANGE_EMAIL_2FA,
  });

  return <Form2FA {...{ ...props }} />;
};

export default Page;
