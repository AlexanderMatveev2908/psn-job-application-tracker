import { authSliceAPI } from "@/features/auth/slices/api";
import { useKitHooks } from "../useKitHooks";
import { usePwdsForm } from "./usePwdsForm";
import { useCheckTypeCbcHmac } from "../tokens/useCheckTypeCbcHmac";
import { TokenT } from "@/common/types/tokens";
import { useUser } from "@/features/user/hooks/useUser";
import { logFormErrs } from "@/core/lib/forms";

type Params = {
  strategy_2FA: boolean;
};

export const useRecoverPwd = ({ strategy_2FA }: Params) => {
  const { formCtx } = usePwdsForm();
  const { handleSubmit } = formCtx;

  const { userState, loginUser, delCbcHmac } = useUser();
  const { nav, wrapAPI } = useKitHooks();

  const [mutate, { isLoading }] = strategy_2FA
    ? authSliceAPI.useRecoverPwdAuthReset2FAMutation()
    : authSliceAPI.useRecoverPwdAuthMutation();

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI({
      cbAPI: () =>
        mutate({ ...data, cbc_hmac_token: userState.cbc_hmac_token }),
    });

    if (!res) return;

    if (res?.access_token) {
      loginUser(res.access_token);
      delCbcHmac();

      nav.replace("/");
    }
  }, logFormErrs);

  useCheckTypeCbcHmac({
    tokenType: strategy_2FA ? TokenT.RECOVER_PWD_2FA : TokenT.RECOVER_PWD,
  });

  return {
    formCtx,
    handleSave,
    isLoading,
  };
};
