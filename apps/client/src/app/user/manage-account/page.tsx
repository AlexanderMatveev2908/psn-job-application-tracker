/** @jsxImportSource @emotion/react */
"use client";

import { TokenT } from "@/common/types/tokens";
import { useListenHeight } from "@/core/hooks/etc/height/useListenHeight";
import { useCheckTypeCbcHmac } from "@/core/hooks/etc/tokens/useCheckTypeCbcHmac";
import { useSwap } from "@/core/hooks/etc/useSwap/useSwap";
import type { FC } from "react";
import ChangeEmailForm from "@/features/user/pages/manage-account/components/ChangeEmailForm";
import ChangePwdForm from "@/features/user/pages/manage-account/components/ChangePwdForm";
import DelAccountSwap from "@/features/user/pages/manage-account/components/DelAccountSwap";
import SwapSetup2FA from "@/features/user/pages/manage-account/components/SwapSetup2FA/SwapSetup2FA";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import WrapMultiFormSwapper from "@/common/components/swap/WrapMultiFormSwapper/WrapMultiFormSwapper";

const Page: FC = () => {
  useCheckTypeCbcHmac({
    tokenType: TokenT.MANAGE_ACC,
    pathPush: "/user/access-manage-account",
  });

  const { startSwap, swapState } = useSwap();
  const { currSwap } = swapState;
  const { user } = useGetUserState();
  const { contentRef, contentH } = useListenHeight({
    opdDep: [currSwap],
  });

  return (
    <WrapMultiFormSwapper
      {...{
        formTestID: "manage_acc",
        propsBtnsSwapper: {
          startSwap,
        },
        propsWrapSwapper: {
          contentH,
        },
        swapState,
        totSwaps: 4,
      }}
    >
      <ChangeEmailForm
        {...{
          contentRef,
          isCurr: !currSwap,
          swapState,
        }}
      />

      <ChangePwdForm
        {...{
          contentRef,
          isCurr: currSwap === 1,
          swapState,
        }}
      />

      <SwapSetup2FA
        {...{
          contentRef,
          isCurr: currSwap === 2,
          swapState,
          user,
        }}
      />

      <DelAccountSwap
        {...{
          contentRef,
          isCurr: currSwap === 3,
        }}
      />
    </WrapMultiFormSwapper>
  );
};

export default Page;
