import { TokenT } from "@/common/types/tokens";
import { verifySliceAPI, VerifyCbcHmacArgT } from "../slices/api";
import { useUser } from "@/features/user/hooks/useUser";
import { useRouter } from "next/navigation";
import { useCallback, useMemo } from "react";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { TagAPI } from "@/common/types/api";
import { useDispatch } from "react-redux";
import { apiSlice } from "@/core/store/api";

export type MapperVerifyT = Record<
  TokenT,
  (cbcHmacToken: string) => Promise<void>
>;

export const useVerify = () => {
  const [triggerRTK] = verifySliceAPI.useLazyVerifyCbcHmacQuery();

  const { loginUser, saveCbcHmac } = useUser();
  const { wrapAPI } = useWrapAPI();
  const dispatch = useDispatch();

  const nav = useRouter();

  const wrapMainCb = useCallback(
    async (data: VerifyCbcHmacArgT) => {
      const res = await wrapAPI({
        cbAPI: () => triggerRTK(data),
        pushNotice: "*",
      });

      return res;
    },
    [, triggerRTK, wrapAPI]
  );

  const loginCb = useCallback(
    (accessToken: string) => {
      loginUser(accessToken);
      dispatch(apiSlice.util.invalidateTags([TagAPI.USER]));
      nav.replace("/");
    },
    [dispatch, nav, loginUser]
  );

  const mapperVerify: MapperVerifyT = useMemo(
    () => ({
      CONF_EMAIL: async (cbcHmacToken: string) => {
        const res = await wrapMainCb({
          cbcHmacToken,
          endpoint: "confirm-email",
        });

        if (!res) return;

        if (res?.accessToken) {
          loginCb(res.accessToken);
        }
      },
      RECOVER_PWD: async (cbcHmacToken: string) => {
        const res = await wrapMainCb({
          cbcHmacToken,
          endpoint: "recover-pwd",
        });

        if (!res) return;

        saveCbcHmac(cbcHmacToken);
        nav.replace(
          res.strategy_2FA
            ? "/verify/recover-password-2FA"
            : "/auth/recover-password"
        );
      },

      CHANGE_EMAIL: async (cbcHmacToken: string) => {
        const res = await wrapMainCb({
          cbcHmacToken,
          endpoint: "new-email",
        });

        if (!res) return;

        if (res?.accessToken) {
          loginCb(res.accessToken);
        } else if (res?.cbcHmacToken) {
          saveCbcHmac(res.cbcHmacToken);
          nav.replace("/verify/change-email-2FA");
        }
      },
    }),
    [wrapMainCb, loginCb, nav, saveCbcHmac]
  ) as MapperVerifyT;

  return {
    mapperVerify,
  };
};
