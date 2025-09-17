/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { SerializedStyles } from "@emotion/react";
import { FieldTxtSvgT, TestIDT } from "@/common/types/ui";
import { AppEventT } from "@/common/types/api";
import WrapElWithShadow from "../shapes/WrapElWithShadow";

type PropsType = {
  handleClick?: () => void;
  href: string;
  download?: string;
  $customLabelCSS?: SerializedStyles;
  el: FieldTxtSvgT;
  act: AppEventT;
} & TestIDT;

const LinkShadow: FC<PropsType> = (arg: PropsType) => {
  return (
    <WrapElWithShadow
      {...{
        ...arg,
        wrapper: "next_link",
      }}
    />
  );
};

export default LinkShadow;
