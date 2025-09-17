/** @jsxImportSource @emotion/react */
"use client";

import { FieldTxtSvgT, JustifyT, SizeT, TestIDT } from "@/common/types/ui";
import type { FC } from "react";
import SpinTxt from "./spinners/SpinTxt";
import { AppEventT } from "@/common/types/api";
import { css, SerializedStyles } from "@emotion/react";

type PropsType = {
  el: FieldTxtSvgT;
  act?: AppEventT;
  isLoading?: boolean;
  isHover?: boolean;
  $SvgSize?: SizeT;
  $labelSize?: SizeT;
  $justify?: JustifyT;
  $ctmLabelCSS?: SerializedStyles;
} & TestIDT;

const PairTxtSvg: FC<PropsType> = ({
  el,
  $SvgSize,
  isLoading,
  isHover,
  act = "NONE",
  $ctmLabelCSS,
  $labelSize,
  $justify,
  testID,
}) => {
  return (
    <div
      className="flex items-center gap-6"
      css={css`
        justify-content: ${$justify ?? "center"};
      `}
    >
      {el.Svg &&
        (isLoading ? (
          <SpinTxt
            {...{
              act,
              isHover,
            }}
          />
        ) : (
          <el.Svg className={$SvgSize ? `svg__${$SvgSize}` : "svg__sm"} />
        ))}

      {el.label && (
        <span
          data-testid={testID}
          className={`${$labelSize ? `txt__${$labelSize}` : "txt__lg"}`}
          css={css`
            ${$ctmLabelCSS}
          `}
        >
          {el.label}
        </span>
      )}
    </div>
  );
};

export default PairTxtSvg;
