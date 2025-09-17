/** @jsxImportSource @emotion/react */
"use client";

import React, { FC, useEffect, useRef, useState } from "react";
import { v4 } from "uuid";
import { AppEventT } from "@/common/types/api";
import { genMinMax } from "@/core/lib/etc";
import { FieldTxtSvgT } from "@/common/types/ui";
import PairTxtSvg from "../elements/PairTxtSvg";
import { $argClr } from "@/core/uiFactory/style";
import { css } from "@emotion/react";
import { useHydration } from "@/core/hooks/etc/hydration/useHydration";
import WrapBtnAPI from "../wrappers/buttons/WrapBtnAPI";

const makeRandomBtn = () => genMinMax(-1200, 1200);

type PropsType = {
  el: FieldTxtSvgT;
  isLoading?: boolean;
  type?: "button" | "submit";
  isEnabled?: boolean;
  act?: AppEventT;
  handleClick?: () => void;
  handleMousePress?: () => void;
};

const BtnBbl: FC<PropsType> = ({
  el,
  isLoading,
  type = "button",
  isEnabled = true,
  act = "NONE",
  handleClick,
  handleMousePress,
}) => {
  const $clr = $argClr[act];

  const btnRef = useRef<HTMLButtonElement | null>(null);
  const [ids] = useState(Array.from({ length: 30 }, () => v4()));

  useEffect(() => {
    const animate = (e: MouseEvent) => {
      if (btnRef.current && btnRef.current.contains(e.target as Node)) {
        let i = 0;

        do {
          const curr = document.getElementById(ids[i]);
          if (!curr) {
            i++;
            continue;
          }

          curr.classList.remove("bubble");
          requestAnimationFrame(() => curr.classList.add("bubble"));
          i++;
        } while (i < ids.length);
      }
    };

    document.addEventListener("mousedown", animate);

    return () => document.removeEventListener("mousedown", animate);
  }, [ids]);

  const { isHydrated } = useHydration();

  return (
    <WrapBtnAPI
      {...{
        act,
        isLoading,
      }}
    >
      <button
        onClick={handleClick}
        onMouseDown={handleMousePress}
        type={type}
        ref={btnRef}
        disabled={!isEnabled}
        className={`btn__app relative w-full border-2 rounded-xl py-2 px-4 flex justify-center items-center`}
        css={css`
          border-color: ${$clr};
          color: ${$clr};
        `}
        style={
          {
            "--scale__up": 1.2,
          } as React.CSSProperties
        }
      >
        {!isHydrated
          ? null
          : ids.map((id, i) => (
              <div
                key={id}
                {...{ id }}
                className={`absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 rounded-full pointer-events-none ${
                  i % 2 === 0
                    ? `w-[10px] h-[10px] border-2`
                    : `h-[7.5px] w-[7.5px]`
                } `}
                css={css`
                  background: ${i % 2 === 0 ? "transparent" : $clr};
                  border-color: ${$clr};
                `}
                style={
                  {
                    "--pos": `${makeRandomBtn()}%, ${makeRandomBtn()}%`,
                    transform: "scale(0)",
                    opacity: "0",
                  } as React.CSSProperties
                }
              ></div>
            ))}

        <PairTxtSvg
          {...{
            el,
          }}
        />
      </button>
    </WrapBtnAPI>
  );
};
export default BtnBbl;
