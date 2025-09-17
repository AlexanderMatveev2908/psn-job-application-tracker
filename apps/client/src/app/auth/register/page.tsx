/** @jsxImportSource @emotion/react */
"use client";

import { type FC } from "react";
import { __cg } from "@/core/lib/log";
import { Path, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useSwap } from "@/core/hooks/etc/useSwap/useSwap";
import BodyFormRegister from "@/features/auth/pages/register/components/BodyFormRegister";
import {
  RegisterFormT,
  registerSchema,
  resetValsRegister,
} from "@/features/auth/pages/register/paperwork";
import { swapOnErr } from "@/core/lib/forms";
import { authSliceAPI } from "@/features/auth/slices/api";
import { useRouter } from "next/navigation";
import { useNotice } from "@/features/notice/hooks/useNotice";
import { useUser } from "@/features/user/hooks/useUser";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { useFocusSwap } from "@/core/hooks/etc/focus/useFocusSwap";
import WrapAuthFormPage from "@/features/auth/components/WrapAuthFormPage";
import { JwtReturnT } from "@/common/types/api";

const Page: FC = () => {
  const formCtx = useForm<RegisterFormT>({
    mode: "onChange",
    resolver: zodResolver(registerSchema),
    defaultValues: resetValsRegister,
  });
  const { setFocus, handleSubmit, reset } = formCtx;

  const [mutate, { isLoading }] = authSliceAPI.useRegisterAuthMutation();
  const { wrapAPI } = useWrapAPI();
  const { setNotice } = useNotice();
  const { loginUser } = useUser();
  const nav = useRouter();

  const kwargs: Path<RegisterFormT>[] = ["first_name", "password"];

  const { startSwap, swapState, lockFocusRef } = useSwap();

  useFocusSwap({
    kwargs,
    setFocus: setFocus,
    swapState,
    lockFocusRef,
  });

  const handleSave = handleSubmit(
    async (data) => {
      const res = await wrapAPI<JwtReturnT>({
        cbAPI: () => mutate(data),
      });

      if (!res?.access_token) return;

      reset(resetValsRegister);

      loginUser(res.access_token);

      setNotice({
        msg: genMailNoticeMsg("to confirm the account"),
        type: "OK",
        child: "OPEN_MAIL_APP",
      });

      nav.replace("/notice");
    },
    (errs) => {
      __cg("errors", errs);

      const res = swapOnErr({
        errs,
        kwargs: [
          ["first_name", "last_name", "email"],
          ["password", "confirm_password", "terms"],
        ],
      });

      if (res?.field) {
        startSwap({ swap: res!.i, lockFocus: true });
        setTimeout(() => {
          setFocus(res.field);
        }, 400);
      }

      return errs;
    }
  );

  return (
    <WrapAuthFormPage
      {...{
        propsProgressSwap: {
          currSwap: swapState.currSwap,
          totSwaps: 2,
        },
        handleSave,
        formCtx,
        propsBtnsSwapper: {
          currSwap: swapState.currSwap,
          startSwap,
          totSwaps: 2,
        },
        isLoading,
        formTestID: "register",
      }}
    >
      <BodyFormRegister
        {...{
          swapState,
        }}
      />
    </WrapAuthFormPage>
  );
};

export default Page;
