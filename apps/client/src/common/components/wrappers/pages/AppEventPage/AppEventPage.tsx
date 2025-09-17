/** @jsxImportSource @emotion/react */
"use client";

import { AppEventT } from "@/common/types/api";
import type { FC } from "react";
import { $argClr } from "@/core/uiFactory/style";
import { ChildrenT } from "@/common/types/ui";
import { SvgsAppEvents } from "./uiFactory";
import BounceIconSSR from "@/common/components/elements/bounceIcons/BounceIconSSR/BounceIconSSR";
import WrapCSR from "../WrapCSR";

type PropsType = {
  act: AppEventT;
  msg: string;
} & ChildrenT;

const AppEventPage: FC<PropsType> = ({ act, msg, children }) => {
  const $clr = $argClr[act];

  return (
    <WrapCSR>
      <div className="w-full h-[75vh] flex flex-col items-center justify-center gap-10 sm:gap-16">
        <BounceIconSSR
          {...{
            act,
            Svg: SvgsAppEvents[act],
          }}
        />

        <div className="w-full flex justify-center max-w-[90%] sm:max-w-[75%]">
          <span
            className="txt__lg"
            style={{
              color: $clr,
            }}
          >
            {msg}
          </span>
        </div>

        {children}
      </div>
    </WrapCSR>
  );
};

export default AppEventPage;
