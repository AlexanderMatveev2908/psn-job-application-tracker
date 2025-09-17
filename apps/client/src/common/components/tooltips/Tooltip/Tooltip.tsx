/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { easeInOut, motion } from "framer-motion";
import { css, SerializedStyles } from "@emotion/react";
import { $argClr } from "@/core/uiFactory/style";
import { AppEventT } from "@/common/types/api";
import TriangleTooltip from "../subComponents/TriangleTooltip";

type PropsType = {
  isHover: boolean;
  $ctmCSS?: SerializedStyles;
  $trgCtmCSS?: SerializedStyles;
  txt?: string | null;
  act?: AppEventT;
};

const Tooltip: FC<PropsType> = ({
  txt,
  $ctmCSS,
  isHover,
  $trgCtmCSS,
  act = "NONE",
}) => {
  const $clr = $argClr[act];

  return (
    <motion.div
      initial={{ opacity: 0, y: 0 }}
      transition={{ duration: 0.3, ease: easeInOut }}
      animate={{
        opacity: isHover ? 1 : 0,
        y: isHover ? "-150%" : 0,
      }}
      className="absolute top-0 min-w-[200px] w-full h-fit max-w-fit py-2 px-4 pointer-events-none z-60 bg-neutral-950 rounded-xl"
      css={css`
        border: 2px solid ${$clr};

        ${$ctmCSS ??
        `
          right: 0;
        `};
      `}
    >
      <div className="w-full flex justify-center">
        <span
          className="txt__sm break-all"
          css={css`
            color: ${$clr};
          `}
        >
          {txt}
        </span>
      </div>

      <TriangleTooltip
        {...{
          $clr,
          $trgCtmCSS,
        }}
      />
    </motion.div>
  );
};

export default Tooltip;
