/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { css } from "@emotion/react";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";

type PropsType = {
  totSwaps: number;
  currSwap: number;
  maxW: number;
};

const ProgressSwap: FC<PropsType> = ({ currSwap, totSwaps, maxW }) => {
  const { ids } = useGenIDs({
    lengths: [totSwaps],
  });

  const rat = (currSwap / (totSwaps - 1)) * 100;

  return (
    <div
      className="w-full max-w-full border-[3px] border-neutral-600 h-[50px] rounded-full flex justify-between mx-auto relative"
      css={css`
        max-width: ${maxW}px;
      `}
    >
      {ids[0].map((id, i) => (
        <div
          key={id}
          className="w-[40px] h-[40px] rounded-full flex items-center justify-center p-5 relative z-60"
          css={css`
            transition: 0.4s;
            border: 2px solid
              var(--${i <= currSwap ? "neutral__950" : "neutral__200"});
            background: var(--${i <= currSwap ? "white__0" : "neutral__950"});
            color: var(--${i <= currSwap ? "neutral__950" : "neutral__200"});
            transform: scale(${currSwap === i ? "1.3" : "1"});
          `}
        >
          <span className="txt__lg">{i + 1}</span>
        </div>
      ))}

      <div className="w-full absolute top-0 left-0 min-h-full min-w-full overflow-hidden rounded-full">
        <div
          className="absolute top-0 left-0 h-full z-30"
          css={css`
            transition: 0.5s;
            background: var(--neutral__200);
            width: ${rat}%;
          `}
        ></div>
      </div>
    </div>
  );
};

export default ProgressSwap;
