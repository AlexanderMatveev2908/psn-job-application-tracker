/** @jsxImportSource @emotion/react */
"use client";

import { type FC } from "react";
import { TestIDT } from "@/common/types/ui";
import WrapElWithSvgTooltip, {
  WrapSvgTltPropsT,
} from "../shapes/WrapElWithSvgTooltip";

type PropsType = {
  handleClick?: () => void;
  isEnabled?: boolean;
} & WrapSvgTltPropsT &
  TestIDT;

const BtnSvg: FC<PropsType> = ({
  handleClick,
  act = "NONE",
  Svg,
  isEnabled = true,
  confPortal,
  testID,
  $SvgSize,
  tooltipTxt,
}) => {
  return (
    <WrapElWithSvgTooltip
      {...{
        wrapper: "html_button",
        propsBtn: {
          handleClick,
          isEnabled,
        },
        confPortal,
        act,
        Svg,
        testID,
        $SvgSize,
        tooltipTxt,
      }}
    />
  );
};

export default BtnSvg;
