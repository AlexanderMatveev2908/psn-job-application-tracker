/** @jsxImportSource @emotion/react */
"use client";

import { useCheckCbcHmac } from "@/core/hooks/etc/tokens/useCheckCbcHmac";
import { useRunOnHydrate } from "@/core/hooks/etc/hydration/useRunOnHydrate";
import { useVerify } from "@/features/verify/hooks/useVerify";
import { useSearchParams } from "next/navigation";
import { useCallback, type FC } from "react";
import WrapCSR from "@/common/components/wrappers/pages/WrapCSR";

const Page: FC = () => {
  const cbc_hmac_token = useSearchParams().get("cbc_hmac_token");

  const { mapperVerify } = useVerify();

  const { checkCbcHmac } = useCheckCbcHmac();

  const cb = useCallback(async () => {
    const aad = checkCbcHmac({ cbc_hmac_token });

    if (aad) await mapperVerify[aad.token_t](cbc_hmac_token!);
  }, [cbc_hmac_token, checkCbcHmac, mapperVerify]);

  useRunOnHydrate({ cb });

  return (
    <WrapCSR
      {...{
        isLoading: true,
      }}
    />
  );
};

export default Page;
