import { TokenT } from "@/common/types/tokens";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { useCallback } from "react";
import { useCheckCbcHmac } from "./useCheckCbcHmac";
import { useRunOnHydrate } from "../hydration/useRunOnHydrate";

type Params = {
  tokenType: TokenT | TokenT[];
  pathPush?: string;
};

export const useCheckTypeCbcHmac = ({ tokenType, pathPush }: Params) => {
  const { cbc_hmac_token } = useGetUserState();
  const { checkCbcHmac } = useCheckCbcHmac();

  const checkCb = useCallback(() => {
    checkCbcHmac({ cbc_hmac_token, tokenType, pathPush });
  }, [checkCbcHmac, cbc_hmac_token, tokenType, pathPush]);

  useRunOnHydrate({ cb: checkCb });
};
