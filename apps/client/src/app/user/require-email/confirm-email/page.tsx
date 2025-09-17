/** @jsxImportSource @emotion/react */
"use client";

import { useEmailForm } from "@/core/hooks/etc/forms/useEmailForm";
import RequireEmailForm from "@/core/forms/RequireEmailForm/RequireEmailForm";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { useRouter } from "next/navigation";
import { useEffect, type FC } from "react";

const Page: FC = () => {
  const { formCtx, handleSaveMaker, isLoading } = useEmailForm();

  const handleSave = handleSaveMaker({
    endpointT: "confirm-email-logged",
    msgNotice: "to confirm the account",
  });

  const isVerified = useGetUserState().user?.is_verified;
  const nav = useRouter();

  useEffect(() => {
    if (isVerified) nav.replace("/");
  }, [isVerified, nav]);

  return (
    <RequireEmailForm
      {...{
        formCtx,
        testID: "conf_email",
        isLoading,
        handleSave,
      }}
    />
  );
};

export default Page;
