/** @jsxImportSource @emotion/react */
"use client";

import { css, SerializedStyles } from "@emotion/react";
import type { FC } from "react";

type PropsType = {
  $clr: string;
  $trgCtmCSS?: SerializedStyles;
  $sizeTrg?: number;
};

const TriangleTooltip: FC<PropsType> = ({
  $clr,
  $trgCtmCSS,
  $sizeTrg = 35,
}) => {
  return (
    <div
      className="absolute top-full overflow-hidden"
      css={css`
        width: ${$sizeTrg}px;
        height: ${$sizeTrg}px;
        ${$trgCtmCSS ??
        `
            right:15%;
          `}
      `}
    >
      <div
        css={css`
          width: ${$sizeTrg}px;
          height: ${$sizeTrg}px;
          border: 2px solid ${$clr};
        `}
        className="absolute rotate-45 bg-neutral-950 translate-y-[-50%] top-[-20%]"
      ></div>
    </div>
  );
};

export default TriangleTooltip;
