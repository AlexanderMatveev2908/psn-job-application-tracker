/** @jsxImportSource @emotion/react */
"use client";

import BoxInput from "@/common/components/forms/inputs/BoxInput";
import { CheckChoiceT } from "@/common/types/ui";
import { css } from "@emotion/react";
import { FieldValues, Path } from "react-hook-form";

type PropsType<T extends FieldValues> = {
  choices: CheckChoiceT[];
  colsForSwap: number;
  isCurr: boolean;
  ids: string[];
  handleClick: (v: T[Path<T>]) => void;
  isCurrChoice: (v: T[Path<T>]) => boolean;
};

const SwapBoxes = <T extends FieldValues>({
  choices,
  colsForSwap,
  isCurr,
  ids,
  handleClick,
  isCurrChoice,
}: PropsType<T>) => {
  return (
    <div
      className="w-full justify-items-center gap-8 h-fit items-start"
      css={css`
        display: grid;
        grid-template-columns: repeat(${colsForSwap}, 1fr);
        transition: 0.3s;
        opacity: ${isCurr ? 1 : 0};
      `}
    >
      {choices.map((ch, idx) => {
        const isChosen = isCurrChoice(ch.val as T[Path<T>]);

        return (
          <div key={ids[idx]} className="w-[250px]">
            <BoxInput
              {...{
                testID: `swap_boxes__${ch.val}`,
                handleClick: () => handleClick(ch.val as T[Path<T>]),
                isChosen,
                opt: ch,
              }}
            />
          </div>
        );
      })}
    </div>
  );
};

export default SwapBoxes;
