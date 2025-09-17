/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import WrapElWithSvgTooltip, {
  WrapSvgTltPropsT,
} from "../shapes/WrapElWithSvgTooltip";

type PropsType = {
  href: string;
} & WrapSvgTltPropsT;

const LinkSvg: FC<PropsType> = ({ Svg, act, confPortal, href, tooltipTxt }) => {
  return (
    <WrapElWithSvgTooltip
      {...{
        wrapper: "next_link",
        Svg,
        act,
        confPortal,
        tooltipTxt,
        propsLink: {
          href,
        },
      }}
    />
  );
};

export default LinkSvg;
