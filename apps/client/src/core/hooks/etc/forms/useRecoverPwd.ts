import { authSliceAPI } from "@/features/auth/slices/api";
import { useKitHooks } from "../useKitHooks";
import { usePwdsForm } from "./usePwdsForm";
import { useCheckTypeCbcHmac } from "../tokens/useCheckTypeCbcHmac";
import { TokenT } from "@/common/types/tokens";
import { useUser } from "@/features/user/hooks/useUser";
import { logFormErrs } from "@/core/lib/forms";

type Params = {
  strategy2FA: boolean;
};

export const useRecoverPwd = ({ strategy2FA }: Params) => {
  const { formCtx } = usePwdsForm();
  const { handleSubmit } = formCtx;

  const { userState, loginUser, delCbcHmac } = useUser();
  const { nav, wrapAPI } = useKitHooks();

  const [mutate, { isLoading }] = strategy2FA
    ? authSliceAPI.useRecoverPwdAuthReset2FAMutation()
    : authSliceAPI.useRecoverPwdAuthMutation();

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI({
      cbAPI: () => mutate({ ...data, cbcHmacToken: userState.cbcHmacToken }),
    });

    if (!res) return;

    if (res?.accessToken) {
      loginUser(res.accessToken);
      delCbcHmac();

      nav.replace("/");
    }
  }, logFormErrs);

  useCheckTypeCbcHmac({
    tokenType: strategy2FA ? TokenT.RECOVER_PWD_2FA : TokenT.RECOVER_PWD,
  });

  return {
    formCtx,
    handleSave,
    isLoading,
  };
};
