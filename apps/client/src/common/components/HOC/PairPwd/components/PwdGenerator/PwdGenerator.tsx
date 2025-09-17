/** @jsxImportSource @emotion/react */
"use client";

import SvgPasswordCursor from "@/common/components/SVGs/PasswordCursor";
import { useState, type FC } from "react";
import BtnSvg from "@/common/components/buttons/BtnSvg";
import { css } from "@emotion/react";
import { resp } from "@/core/lib/style";
import { genPwd } from "@/core/lib/pwd";
import { SwapModeT } from "@/core/hooks/etc/useSwap/etc/initState";
import CpyPaste from "@/common/components/HOC/CpyPaste/CpyPaste";
import { isStr } from "@/core/lib/dataStructure/ect";

type PropsType = {
  swapMode?: SwapModeT;
  isCurrSwap?: boolean;
};

const PwdGenerator: FC<PropsType> = ({
  swapMode = "swapped",
  isCurrSwap = true,
}) => {
  const [pwd, setPwd] = useState("");

  return (
    <div
      className="w-full grid"
      css={css`
        grid-template-columns: 1fr;
        gap: 1.5rem;

        ${resp(450)} {
          grid-template-columns: 75px 1fr;
        }
      `}
    >
      <div className="w-fit">
        <BtnSvg
          {...{
            testID: "pwd_generator__btn",
            Svg: SvgPasswordCursor,
            handleClick: () => setPwd(genPwd()),
            confPortal: {
              optDep: [swapMode],
              showPortal: swapMode === "swapped" && isCurrSwap,
            },
            tooltipTxt: "Generate Password",
          }}
        />
      </div>

      {isStr(pwd) && (
        <div className="w-[250px]">
          <CpyPaste
            {...{
              testID: "pwd_generator__result",
              txt: pwd,
              portalConf: {
                optDep: [swapMode],
                showPortal: swapMode === "swapped" && isCurrSwap,
              },
            }}
          />
        </div>
      )}
    </div>
  );
};

export default PwdGenerator;
