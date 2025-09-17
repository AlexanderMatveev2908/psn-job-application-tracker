/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { easeInOut, motion } from "framer-motion";
import { css, SerializedStyles } from "@emotion/react";
import { IconType } from "react-icons";

type PropsType = {
  $customCSS: SerializedStyles;
  Svg: IconType;
};

const BounceIconCSR: FC<PropsType> = ({ $customCSS, Svg }) => {
  return (
    <motion.div
      className="w-full flex justify-center"
      transition={{ duration: 0.8, ease: easeInOut }}
      initial={{ scaleX: 0, scaleY: 0 }}
      animate={{
        scaleX: [1.6, 0.6, 1.3, 0.9, 1.05, 1],
        scaleY: [0.4, 1.4, 0.7, 1.2, 0.95, 1],
      }}
    >
      <Svg
        css={css`
          ${$customCSS}
        `}
      />
    </motion.div>
  );
};

export default BounceIconCSR;
