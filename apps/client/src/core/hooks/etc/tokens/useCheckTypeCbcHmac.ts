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
  const { cbcHmacToken } = useGetUserState();
  const { checkCbcHmac } = useCheckCbcHmac();

  const checkCb = useCallback(() => {
    checkCbcHmac({ cbcHmacToken, tokenType, pathPush });
  }, [checkCbcHmac, cbcHmacToken, tokenType, pathPush]);

  useRunOnHydrate({ cb: checkCb });
};
