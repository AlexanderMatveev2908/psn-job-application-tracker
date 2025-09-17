/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import Portal from "../wrappers/portals/Portal";
import { css } from "@emotion/react";
import { SerializedStyles } from "@emotion/react";
import { ChildrenT } from "@/common/types/ui";
import { AppEventT } from "@/common/types/api";
import { $argClr } from "@/core/uiFactory/style";
import TriangleTooltip from "./subComponents/TriangleTooltip";

type PropsType = {
  $CSS: SerializedStyles;
  isHover: boolean;
  act?: AppEventT;
  $trgCtmCSS?: SerializedStyles;
  $sizeTrg?: number;
} & ChildrenT;

const PortalTooltip: FC<PropsType> = ({
  $CSS,
  $trgCtmCSS,
  $sizeTrg = 50,
  children,
  act = "NONE",
  isHover,
}) => {
  const $clr = $argClr[act];

  return (
    <Portal>
      <div
        className="absolute w-fit h-fit bg-neutral-950 border-2 rounded-xl pointer-events-none z-60"
        css={css`
          ${$CSS}
          border-color: ${$clr};
          transition: transform 0.4s, opacity 0.3s;
          transform: translateY(${isHover ? "-100" : "0"}%);
          opacity: ${isHover ? 1 : 0};
          z-index: 100;
        `}
      >
        {children}

        <TriangleTooltip
          {...{
            $clr,
            $sizeTrg,
            $trgCtmCSS,
          }}
        />
      </div>
    </Portal>
  );
};

export default PortalTooltip;
