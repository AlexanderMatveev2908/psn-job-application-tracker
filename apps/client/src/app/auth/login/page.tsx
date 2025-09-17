"use client";

import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { useFocus } from "@/core/hooks/etc/focus/useFocus";
import BodyFormLogin from "@/features/auth/pages/login/components/BodyFormLogin";
import {
  LoginFormT,
  loginSchema,
  resetValsLogin,
} from "@/features/auth/pages/login/paperwork";
import { authSliceAPI, LoginUserReturnT } from "@/features/auth/slices/api";
import { useUser } from "@/features/user/hooks/useUser";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { FC } from "react";
import { useForm } from "react-hook-form";
import WrapAuthFormPage from "@/features/auth/components/WrapAuthFormPage";
import { logFormErrs } from "@/core/lib/forms";

const Page: FC = () => {
  const { wrapAPI } = useWrapAPI();
  const nav = useRouter();
  const [mutate, { isLoading }] = authSliceAPI.useLoginAuthMutation();
  const { loginUser, saveCbcHmac } = useUser();

  const formCtx = useForm<LoginFormT>({
    mode: "onChange",
    resolver: zodResolver(loginSchema),
    defaultValues: resetValsLogin,
  });
  const { handleSubmit, setFocus, reset } = formCtx;

  const handleSave = handleSubmit(async (data) => {
    const res = await wrapAPI<LoginUserReturnT>({
      cbAPI: () => mutate(data),
    });

    if (!res) return;

    if (res?.access_token) loginUser(res.access_token);
    else if (res?.cbc_hmac_token) saveCbcHmac(res.cbc_hmac_token);

    reset(resetValsLogin);

    nav.replace(res?.access_token ? "/" : "/auth/login-2FA");
  }, logFormErrs);

  useFocus("email", { setFocus });

  return (
    <WrapAuthFormPage
      {...{
        formCtx,
        handleSave,
        formTestID: "login",
        isLoading,
      }}
    >
      <BodyFormLogin />
    </WrapAuthFormPage>
  );
};

export default Page;
