/** @jsxImportSource @emotion/react */
"use client";

import { useHydration } from "@/core/hooks/etc/hydration/useHydration";
import { ReactNode, type FC } from "react";
import { createPortal } from "react-dom";

type PropsType = {
  children: ReactNode;
  needEvents?: boolean;
};

const Portal: FC<PropsType> = ({ children, needEvents }) => {
  const { isHydrated } = useHydration();
  if (!isHydrated) return null;

  const portal = needEvents
    ? document.body
    : document.getElementById("portal-root") ?? document.body;

  return createPortal(children, portal);
};

export default Portal;
