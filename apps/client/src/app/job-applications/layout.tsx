/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { useLockLayoutLogged } from "@/core/hooks/etc/navigation/useLockLayoutLogged";
import { type FC } from "react";

const Layout: FC<ChildrenT> = ({ children }) => {
  useLockLayoutLogged();

  return children;
};

export default Layout;
