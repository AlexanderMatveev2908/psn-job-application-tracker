/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { SerializedStyles } from "@emotion/react";
import { FieldTxtSvgT, TestIDT } from "@/common/types/ui";
import { AppEventT } from "@/common/types/api";
import WrapElWithShadow from "../shapes/WrapElWithShadow";

type PropsType = {
  handleClick?: () => void;
  $customLabelCSS?: SerializedStyles;
  isEnabled?: boolean;
  isLoading?: boolean;
  el: FieldTxtSvgT;
  act: AppEventT;
  type?: "submit" | "button";
  isTxtLoading?: boolean;
} & TestIDT;

const BtnShadow: FC<PropsType> = (arg: PropsType) => {
  return (
    <WrapElWithShadow
      {...{
        ...arg,
        wrapper: "html_button",
      }}
    />
  );
};

export default BtnShadow;
