/** @jsxImportSource @emotion/react */
"use client";

import { CheckChoiceT, TestIDT, SizeT } from "@/common/types/ui";
import { css, SerializedStyles } from "@emotion/react";
import type { FC } from "react";
import PairTxtSvg from "../../elements/PairTxtSvg";

type PropsType = {
  handleClick: () => void;
  isChosen: boolean;
  opt: CheckChoiceT;
  $ctmLabelCSS?: SerializedStyles;
  $labelSize?: SizeT;
} & TestIDT;

const BoxInput: FC<PropsType> = ({
  testID,
  isChosen,
  $ctmLabelCSS,
  handleClick,
  opt,
  $labelSize,
}) => {
  return (
    <button
      data-testid={testID}
      type="button"
      className="w-full rounded-xl py-3 px-4 flex justify-center items-center gap-6 h-fit"
      css={css`
        transition: ${isChosen ? 0.2 : 0.3}s ease-in-out;
        border: 2px solid var(--${isChosen ? "white__0" : "neutral__600"});
        background: var(--${isChosen ? "white__0" : "transparent"});
        transform: scale(${isChosen ? 0.85 : 1});
        cursor: pointer;
        color: var(--${isChosen ? "neutral__950" : "neutral__300"});

        &:hover {
          transform: scale(${isChosen ? 0.85 : 1.15});
        }
      `}
      onClick={handleClick}
    >
      <PairTxtSvg
        {...{
          el: {
            label: opt.label,
            Svg: opt.Svg,
          },
          $ctmLabelCSS,
          $labelSize,
        }}
      />
    </button>
  );
};

export default BoxInput;
