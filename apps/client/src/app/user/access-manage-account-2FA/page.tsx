/** @jsxImportSource @emotion/react */
"use client";

import { UnwrappedResApiT } from "@/common/types/api";
import { TokenT } from "@/common/types/tokens";
import Form2FA from "@/core/forms/Form2FA/Form2FA";
import { use2FAForm } from "@/core/hooks/etc/forms/use2FAForm";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { usePushOnCbcHmacPresent } from "@/features/user/hooks/usePushOnCbcHmacPresent";
import { useUser } from "@/features/user/hooks/useUser";
import {
  GainAccessManageAccReturnT,
  userSliceAPI,
} from "@/features/user/slices/api";
import { useCallback, type FC } from "react";

const Page: FC = () => {
  const [mutate] = userSliceAPI.useGetAccessManageAcc2FAMutation();
  const { nav } = useKitHooks();
  const { saveCbcHmac } = useUser();

  const successCb = useCallback(
    async (res: UnwrappedResApiT<GainAccessManageAccReturnT>) => {
      saveCbcHmac(res.cbc_hmac_token);

      nav.replace("/user/manage-account");
    },
    [saveCbcHmac, nav]
  );

  const props = use2FAForm({
    mutationTrigger: mutate,
    successCb,
    delCbcOnSuccess: false,
    tokenType: TokenT.MANAGE_ACC_2FA,
  });

  usePushOnCbcHmacPresent({ tokenType: TokenT.MANAGE_ACC });

  return <Form2FA {...{ ...props }} />;
};

export default Page;
