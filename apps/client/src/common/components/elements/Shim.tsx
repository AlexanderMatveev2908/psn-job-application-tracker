/** @jsxImportSource @emotion/react */
"use client";

import { SerializedStyles } from "@emotion/react";
import type { FC } from "react";

type PropsType = {
  $CSS: SerializedStyles;
};

const Shim: FC<PropsType> = ({ $CSS }) => {
  return (
    <div
      className="skeleton mx-auto border-[3px] border-neutral-600 p-5 rounded-xl relative"
      css={$CSS}
    ></div>
  );
};

export default Shim;
