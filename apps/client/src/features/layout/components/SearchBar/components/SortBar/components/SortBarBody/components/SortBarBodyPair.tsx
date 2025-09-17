/** @jsxImportSource @emotion/react */
"use client";

import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import BoxInput from "@/common/components/forms/inputs/BoxInput";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { resp } from "@/core/lib/style";
import { SorterSearchBarT } from "@/features/layout/components/SearchBar/types";
import { css } from "@emotion/react";
import type { FC } from "react";
import { FaSortAmountDown, FaSortAmountUp } from "react-icons/fa";

export type ArgFnSorter = {
  el: SorterSearchBarT;
  val: "ASC" | "DESC";
};

type PropsType = {
  el: SorterSearchBarT;
  handleClick: (arg: ArgFnSorter) => void;
  currVal: string;
};

const SortBarBodyPair: FC<PropsType> = ({ el, currVal, handleClick }) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [2] });

  return (
    <div
      data-testid={`body__${el.name}`}
      className="w-full grid grid-cols-1 gap-6 justify-items-center"
    >
      <PairTxtSvg
        {...{
          el: {
            label: el.label,
            Svg: el.Svg,
          },
        }}
      />

      <div className="w-full grid grid-cols-2 gap-6 justify-items-center">
        {ids.map((id, idx) => {
          const val = !idx ? "ASC" : "DESC";

          return (
            <div key={id} className="w-[75px] md:w-[200px]">
              <BoxInput
                {...{
                  isChosen: currVal === val,
                  handleClick: () => handleClick({ el, val }),
                  opt: {
                    label: val,
                    val,
                    Svg: !idx ? FaSortAmountUp : FaSortAmountDown,
                  },
                  $ctmLabelCSS: css`
                    display: none;

                    ${resp("md")} {
                      display: block;
                    }
                  `,
                  testID: `sort_bar__${el.name}__${val}`,
                }}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default SortBarBodyPair;
