import { TokenT } from "@/common/types/tokens";
import { extractAadFromCbcHmac } from "@/core/lib/dataStructure/parsers";
import { useUser } from "@/features/user/hooks/useUser";
import { usePathname } from "next/navigation";
import { useEffect } from "react";
import { useWrapAPI } from "../../../core/hooks/api/useWrapAPI";
import { cleanupSliceAPI } from "@/features/cleanup/slices/api";

const mapper: Record<TokenT, string | string[]> = {
  [TokenT.MANAGE_ACC]: "/user/manage-account",
  [TokenT.LOGIN_2FA]: "/auth/login-2FA",
  [TokenT.MANAGE_ACC_2FA]: "/user/access-manage-account-2FA",
  [TokenT.CHANGE_EMAIL_2FA]: "/verify/change-email-2FA",
  [TokenT.RECOVER_PWD]: [
    "/auth/recover-password",
    "/verify/recover-password-2FA",
  ],
  [TokenT.RECOVER_PWD_2FA]: "/auth/recover-password-2FA",
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
} as any;

export const useDelCbcHmacByPathAndType = () => {
  const { userState, delCbcHmac } = useUser();

  const p = usePathname();

  const { wrapAPI } = useWrapAPI();
  const [mutate] = cleanupSliceAPI.useCleanCbcHmacMutation();

  useEffect(() => {
    const cb = async () => {
      if (!userState.cbc_hmac_token || userState.pendingActionCbcHmac) return;
      const aad = extractAadFromCbcHmac(userState.cbc_hmac_token);
      if (!aad) return;

      const { token_t } = aad;

      if (
        (Array.isArray(mapper[token_t]) &&
          mapper[token_t].some((allowed) => p.startsWith(allowed))) ||
        (typeof mapper[token_t] === "string" && p.startsWith(mapper[token_t]))
      )
        return;

      delCbcHmac();

      await wrapAPI({
        cbAPI: () => mutate(userState.cbc_hmac_token),
        showToast: false,
        hideErr: true,
      });
    };

    cb();
  }, [
    userState.cbc_hmac_token,
    delCbcHmac,
    p,
    wrapAPI,
    mutate,
    userState.pendingActionCbcHmac,
  ]);
};
