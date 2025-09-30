/** @jsxImportSource @emotion/react */
"use client";

import { useCheckCbcHmac } from "@/core/hooks/etc/tokens/useCheckCbcHmac";
import { useRunOnHydrate } from "@/core/hooks/etc/hydration/useRunOnHydrate";
import { useVerify } from "@/features/verify/hooks/useVerify";
import { useSearchParams } from "next/navigation";
import { useCallback, type FC } from "react";
import WrapCSR from "@/common/components/wrappers/pages/WrapCSR";

const Page: FC = () => {
  const cbcHmacToken = useSearchParams().get("cbcHmacToken");

  const { mapperVerify } = useVerify();

  const { checkCbcHmac } = useCheckCbcHmac();

  const cb = useCallback(async () => {
    const aad = checkCbcHmac({ cbcHmacToken });

    if (aad) await mapperVerify[aad.tokenT](cbcHmacToken!);
  }, [cbcHmacToken, checkCbcHmac, mapperVerify]);

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
