/** @jsxImportSource @emotion/react */
"use client";

import { css } from "@emotion/react";
import type { FC } from "react";

type PropsType = {
  isDark: boolean | null;
  classIndexCSS: string;
};

const BlackBg: FC<PropsType> = ({ isDark, classIndexCSS }) => {
  return (
    <div
      className={`bg-black/70 fixed inset-0 ${classIndexCSS}`}
      css={css`
        pointer-events: ${isDark ? "all" : "none"};
        opacity: ${isDark ? 1 : 0};
        transition: 0.1s ease-in-out;
      `}
    ></div>
  );
};

export default BlackBg;
