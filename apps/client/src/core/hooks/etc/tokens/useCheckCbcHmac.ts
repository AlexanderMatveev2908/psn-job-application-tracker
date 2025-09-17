import { TokenT } from "@/common/types/tokens";
import { extractAadFromCbcHmac } from "@/core/lib/dataStructure/parsers";
import { useNotice } from "@/features/notice/hooks/useNotice";
import { useRouter } from "next/navigation";
import { useCallback } from "react";

export const useCheckCbcHmac = () => {
  const { setNotice } = useNotice();

  const nav = useRouter();

  const checkCbcHmac = useCallback(
    ({
      cbc_hmac_token,
      tokenType,
      pathPush,
    }: {
      cbc_hmac_token?: string | null;
      tokenType?: TokenT | TokenT[];
      pathPush?: string;
    }) => {
      const aad = extractAadFromCbcHmac(cbc_hmac_token);

      if (!aad || !Object.values(TokenT).includes(aad.token_t)) {
        if (pathPush) {
          nav.replace(pathPush);
        } else {
          setNotice({
            msg: cbc_hmac_token ? `Invalid token format` : "Token not provided",
            type: "ERR",
          });
          nav.replace("/notice");
        }

        return;
      }

      if (
        tokenType &&
        ((typeof tokenType === "string" && aad.token_t !== tokenType) ||
          (Array.isArray(tokenType) && !tokenType.includes(aad.token_t)))
      ) {
        if (pathPush) {
          nav.replace(pathPush);
        } else {
          setNotice({
            msg: `Invalid token type`,
            type: "ERR",
          });
          nav.replace("/notice");
        }

        return;
      }

      return aad;
    },
    [nav, setNotice]
  );

  return {
    checkCbcHmac,
  };
};
