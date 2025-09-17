/** @jsxImportSource @emotion/react */
"use client";

import { useWrapQuery } from "@/core/hooks/api/useWrapQuery";
import { testSliceAPI } from "@/features/test/slices/api";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { useRouter } from "next/navigation";
import { useEffect, type FC } from "react";

const Page: FC = () => {
  const canPushNonLogged = useGetUserState().canPushNonLogged;

  const nav = useRouter();

  const res = testSliceAPI.useGetProtectedQuery();
  useWrapQuery(res);

  useEffect(() => {
    if (canPushNonLogged) nav.replace("/auth/login");
  }, [canPushNonLogged, nav]);

  return <div></div>;
};

export default Page;
