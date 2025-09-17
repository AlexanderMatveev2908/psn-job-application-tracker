/** @jsxImportSource @emotion/react */
"use client";

import PortalTooltip from "@/common/components/tooltips/PortalTooltip";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { css } from "@emotion/react";
import type { FC } from "react";
import { rulesPwd, lengthPwd, extractClr } from "./uiFactory/idx";
import { CoordsTooltipT } from "@/core/hooks/etc/tooltips/useSyncPortal";
import { REG_PWD } from "@/core/constants/regex";
import { SwapModeT } from "@/core/hooks/etc/useSwap/etc/initState";

type PropsType = {
  coords: CoordsTooltipT;
  isCurrSwap: boolean;
  isFocus: boolean;
  isDirty: boolean;
  pwd?: string;
  swapMode?: SwapModeT;
};

const PwdMatchTracker: FC<PropsType> = ({
  coords,
  swapMode = "swapped",
  isCurrSwap,
  isFocus,
  pwd = "",
  isDirty,
}) => {
  const { ids } = useGenIDs({ lengths: [rulesPwd.length] });

  return (
    <PortalTooltip
      {...{
        $CSS: css`
          top: ${coords.top - 50}px;
          left: ${coords.left - 15}px;
        `,
        act: !isDirty ? "NONE" : REG_PWD.test(pwd) ? "OK" : "ERR",
        $trgCtmCSS: css`
          left: 15%;
        `,
        isHover: isCurrSwap && swapMode === "swapped" && isFocus,
      }}
    >
      <div className="w-[75vw] max-w-[800px] p-5 grid grid-cols-1 gap-6">
        <div
          className="grid justify-items-center gap-6"
          css={css`
            grid-template-columns: repeat(auto-fit, minmax(75px, 1fr));
          `}
        >
          {rulesPwd.map((el, i) => (
            <div
              key={ids[0][i]}
              className={`flex w-[75px] items-center justify-center border-2 rounded-xl py-2 transition-all duration-300 ${
                !isDirty ? "text-w__0" : extractClr(el.reg, pwd)
              }`}
            >
              <el.Svg className="svg__sm" />
            </div>
          ))}

          <div
            className={`flex w-[150px] items-center justify-center gap-6 border-2 rounded-xl py-2 col-span-2 transition-all duration-300 ${
              !isDirty ? "text-w__0" : extractClr(lengthPwd.reg, pwd)
            }`}
          >
            <lengthPwd.Svg className="svg__sm" />

            <span className="txt__lg">{pwd?.trim()?.length ?? 0}/8</span>
          </div>
        </div>
      </div>
    </PortalTooltip>
  );
};

export default PwdMatchTracker;
