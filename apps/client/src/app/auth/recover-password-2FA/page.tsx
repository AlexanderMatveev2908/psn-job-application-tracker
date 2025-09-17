/** @jsxImportSource @emotion/react */
"use client";

import FormResetPwd from "@/core/forms/FormResetPwd/FormResetPwd";
import { useRecoverPwd } from "@/core/hooks/etc/forms/useRecoverPwd";
import type { FC } from "react";

const Page: FC = () => {
  const { handleSave, isLoading, formCtx } = useRecoverPwd({
    strategy_2FA: true,
  });
  return (
    <FormResetPwd
      {...{
        handleSave,
        formCtx,
        testID: "recover_pwd",
        isLoading,
      }}
    />
  );
};

export default Page;
