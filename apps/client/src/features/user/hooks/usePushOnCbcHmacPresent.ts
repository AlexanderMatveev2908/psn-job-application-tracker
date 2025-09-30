import { useEffect } from "react";
import { useUser } from "./useUser";
import { useRouter } from "next/navigation";
import { extractAadFromCbcHmac } from "@/core/lib/dataStructure/parsers";
import { TokenT } from "@/common/types/tokens";

type Params = {
  tokenType: TokenT;
};

export const usePushOnCbcHmacPresent = ({ tokenType }: Params) => {
  const {
    delCbcHmac,
    userState: { cbcHmacToken, pendingActionCbcHmac },
  } = useUser();

  const nav = useRouter();

  useEffect(() => {
    if (
      extractAadFromCbcHmac(cbcHmacToken)?.tokenT !== tokenType ||
      pendingActionCbcHmac
    )
      return;

    delCbcHmac();
    nav.replace("/");
  }, [cbcHmacToken, delCbcHmac, nav, pendingActionCbcHmac, tokenType]);
};
