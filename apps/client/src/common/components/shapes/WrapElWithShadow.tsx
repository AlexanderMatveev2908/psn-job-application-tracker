/** @jsxImportSource @emotion/react */
"use client";

import { AppEventT } from "@/common/types/api";
import { css, SerializedStyles } from "@emotion/react";
import { CSSProperties, useMemo, type FC } from "react";
import { $argClr } from "@/core/uiFactory/style";
import Link from "next/link";
import { FieldTxtSvgT, TestIDT } from "@/common/types/ui";
import WrapBtnAPI from "../wrappers/buttons/WrapBtnAPI";
import PairTxtSvg from "../elements/PairTxtSvg";

type PropsType = {
  wrapper: "next_link" | "html_button";
  handleClick?: () => void;
  href?: string;
  download?: string;
  $customLabelCSS?: SerializedStyles;
  isEnabled?: boolean;
  isLoading?: boolean;
  el: FieldTxtSvgT;
  act: AppEventT;
  type?: "submit" | "button";
  isTxtLoading?: boolean;
} & TestIDT;

const WrapElWithShadow: FC<PropsType> = ({
  wrapper,
  $customLabelCSS,
  act = "NONE",
  handleClick,
  href,
  download,
  isEnabled = true,
  isLoading,
  el,
  testID,
  type = "button",
  isTxtLoading,
}) => {
  const $clr = $argClr[act];

  const objProps = useMemo(
    () => ({
      "data-testid": testID,
      className: `${
        wrapper === "next_link" ? "el__app" : "btn__app"
      } w-full flex justify-center items-center gap-6 py-2 px-4 rounded-xl`,

      style: {
        "--scale__up": 1.15,
      } as CSSProperties,

      css: css`
        border: 2px solid ${$clr};
        color: ${$clr};
        background: transparent;

        &${wrapper === "html_button" ? ":enabled" : ""}:hover {
          box-shadow: 0 0 5px ${$clr}, 0 0 10px ${$clr}, 0 0 15px ${$clr},
            0 0 20px ${$clr}, 0 0 25px ${$clr}, 0 0 30px ${$clr};
        }
      `,
    }),
    [$clr, wrapper, testID]
  );

  const content = (
    <>
      <PairTxtSvg
        {...{
          el,
          $ctmLabelCSS: $customLabelCSS,
          act,
          isLoading: isTxtLoading,
        }}
      />
    </>
  );

  return wrapper === "next_link" ? (
    <Link
      href={href!}
      {...{
        ...(download ? { download } : {}),
      }}
      {...objProps}
      {...(href?.startsWith("https://")
        ? { rel: "noopener noreferrer", target: "_blank" }
        : {})}
    >
      {content}
    </Link>
  ) : (
    <WrapBtnAPI {...{ isLoading, act }}>
      <button
        type={type}
        onClick={handleClick}
        disabled={!isEnabled}
        {...objProps}
      >
        {content}
      </button>
    </WrapBtnAPI>
  );
};

export default WrapElWithShadow;
