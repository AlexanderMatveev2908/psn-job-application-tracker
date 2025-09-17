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
    userState: { cbc_hmac_token, pendingActionCbcHmac },
  } = useUser();

  const nav = useRouter();

  useEffect(() => {
    if (
      extractAadFromCbcHmac(cbc_hmac_token)?.token_t !== tokenType ||
      pendingActionCbcHmac
    )
      return;

    delCbcHmac();
    nav.replace("/");
  }, [cbc_hmac_token, delCbcHmac, nav, pendingActionCbcHmac, tokenType]);
};
