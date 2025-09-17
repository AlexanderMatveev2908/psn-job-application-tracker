/** @jsxImportSource @emotion/react */
"use client";

import type { Dispatch, FC, SetStateAction } from "react";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { PayloadStartSwapT } from "@/core/hooks/etc/useSwap/etc/initState";
import BtnShadow from "@/common/components/buttons/BtnShadow";

export type PropsTypeBtnsSwapper = {
  totSwaps: number;
  currSwap: number;
  startSwap?: (v: PayloadStartSwapT) => void;
  setCurrSwap?: Dispatch<SetStateAction<number>>;
};

const BtnsSwapper: FC<PropsTypeBtnsSwapper> = ({
  currSwap,
  startSwap,
  setCurrSwap,
  totSwaps,
}) => {
  const { ids } = useGenIDs({ lengths: [2] });

  return (
    <div className="w-full grid grid-cols-2">
      {ids[0].map((id, i) => (
        <div
          key={id}
          className={`w-[75px] ${
            !i ? "justify-self-start" : "justify-self-end"
          }`}
        >
          <BtnShadow
            {...{
              testID: !i ? "btns_swapper_prev_swap" : "btns_swapper_next_swap",
              act: "NONE",
              el: { Svg: !i ? ChevronLeft : ChevronRight },
              isEnabled: !i ? currSwap >= 1 : currSwap + 1 < totSwaps,
              handleClick: () => {
                const val = !i ? currSwap - 1 : currSwap + 1;

                if (typeof startSwap === "function") startSwap({ swap: val });
                else if (typeof setCurrSwap === "function") setCurrSwap(val);
              },
            }}
          />
        </div>
      ))}
    </div>
  );
};

export default BtnsSwapper;
