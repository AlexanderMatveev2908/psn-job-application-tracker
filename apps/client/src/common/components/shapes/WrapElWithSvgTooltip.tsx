/** @jsxImportSource @emotion/react */
"use client";

import { AppEventT } from "@/common/types/api";
import { PortalConfT, SizeT, TestIDT } from "@/common/types/ui";
import { CSSProperties, useMemo, useState, type FC } from "react";
import { IconType } from "react-icons";
import { $argClr } from "@/core/uiFactory/style";
import { useSyncPortal } from "@/core/hooks/etc/tooltips/useSyncPortal";
import { css } from "@emotion/react";
import Link from "next/link";
import { RefObject } from "react";
import PortalTooltip from "../tooltips/PortalTooltip";
import { isObjOk } from "@/core/lib/dataStructure/ect";
import Tooltip from "../tooltips/Tooltip/Tooltip";

export type WrapSvgTltPropsT = {
  Svg: IconType;
  tooltipTxt?: string;
  act?: AppEventT;
  confPortal?: PortalConfT;
  $SvgSize?: SizeT;
};

type PropsType = {
  wrapper: "html_button" | "next_link";
  propsLink?: {
    href: string;
  };
  propsBtn?: {
    isEnabled?: boolean;
    handleClick?: () => void;
  };
} & WrapSvgTltPropsT &
  TestIDT;

const WrapElWithSvgTooltip: FC<PropsType> = ({
  Svg,
  act = "NONE",
  confPortal,
  wrapper,
  propsLink,
  propsBtn,
  testID,
  $SvgSize,
  tooltipTxt,
}) => {
  const [isHover, setIsHover] = useState(false);
  const $clr = $argClr[act];

  const { coords, parentRef } = useSyncPortal(confPortal?.optDep);

  const objProps = useMemo(
    () => ({
      "data-testid": testID,
      onMouseEnter: () => setIsHover(true),
      onMouseLeave: () => setIsHover(false),
      ref: parentRef,
      className: `${
        tooltipTxt
          ? wrapper === "next_link" ||
            typeof propsBtn?.handleClick === "function"
            ? "cursor-pointer"
            : ""
          : wrapper === "html_button"
          ? "btn__app"
          : "el__app"
      } flex items-center gap-6 justify-center relative`,
      css: css`
        color: ${$clr};
      `,
      style: {
        "--scale__up": 1.3,
      } as CSSProperties,
    }),
    [$clr, tooltipTxt, propsBtn?.handleClick, parentRef, wrapper, testID]
  );

  const content = (
    <>
      {!tooltipTxt ? null : isObjOk(confPortal) ? (
        <PortalTooltip
          {...{
            isHover: isHover && confPortal!.showPortal,
            act,
            $CSS: css`
              top: ${coords.top - coords.height / 2}px;
              left: ${coords.left - coords.width / 2}px;
            `,
            $trgCtmCSS: css`
              left: 15%;
            `,
            $sizeTrg: 30,
          }}
        >
          <span className="txt__md py-2 px-4 inline-block max-w-[300px] break-all">
            {tooltipTxt}
          </span>
        </PortalTooltip>
      ) : (
        <Tooltip
          {...{
            txt: tooltipTxt,
            isHover,
            act,
            $ctmCSS: css`
              right: -50%;
              top: 20%;
            `,
          }}
        />
      )}

      <Svg className={`${$SvgSize ? `svg__${$SvgSize}` : "svg__lg"} z-10`} />
    </>
  );

  return wrapper === "html_button" ? (
    <button
      type="button"
      disabled={!propsBtn!.isEnabled}
      onClick={propsBtn!.handleClick}
      {...(objProps as typeof objProps & { ref: RefObject<HTMLButtonElement> })}
    >
      {content}
    </button>
  ) : (
    <Link
      href={propsLink!.href}
      {...(objProps as typeof objProps & { ref: RefObject<HTMLAnchorElement> })}
    >
      {content}
    </Link>
  );
};

export default WrapElWithSvgTooltip;
