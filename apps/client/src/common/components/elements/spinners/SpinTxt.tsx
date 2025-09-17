/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { motion } from "framer-motion";
import { css } from "@emotion/react";
import { $argClr } from "@/core/uiFactory/style";
import { AppEventT } from "@/common/types/api";

type PropsType = {
  act?: AppEventT;
  isHover?: boolean;
};

const SpinTxt: FC<PropsType> = ({ act = "NONE", isHover }) => {
  let $clr = $argClr[act];

  if (isHover && act === "NONE") $clr = "var(--neutral__950)";

  return (
    <motion.div
      initial={{
        rotate: 0,
      }}
      transition={{
        duration: 1,
        ease: "linear",
        repeat: Infinity,
      }}
      animate={{
        rotate: 360,
      }}
      className="min-w-[35px] min-h-[35px]"
      css={css`
        border: 3px solid ${$clr};
        border-left-color: transparent;
        border-right-color: transparent;
        border-radius: 999px;
      `}
    ></motion.div>
  );
};

export default SpinTxt;
