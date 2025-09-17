"use client";

import BaseLayoutPage from "@/common/components/wrappers/pages/BaseLayoutPage";
import { ChildrenT } from "@/common/types/ui";
import { extractNamePagePath } from "@/core/lib/path";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, type FC } from "react";

const Layout: FC<ChildrenT> = ({ children }) => {
  const p = usePathname();

  const nav = useRouter();
  const usState = useGetUserState();

  useEffect(() => {
    if (usState.isLogged && !usState.pendingActionSession) nav.replace("/");
  }, [usState.isLogged, usState.pendingActionSession, nav]);

  return (
    <BaseLayoutPage
      {...{
        title: extractNamePagePath(p, { usFriendly: true }),
      }}
    >
      {children}
    </BaseLayoutPage>
  );
};

export default Layout;
