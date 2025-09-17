"use client";

import { AppEventT } from "@/common/types/api";
import type { FC } from "react";
import { IconType } from "react-icons";
import { $argClr } from "@/core/uiFactory/style";
import s from "./BounceIconSSR.module.css";

type PropsType = {
  act: AppEventT;
  Svg: IconType;
};

const BounceIconSSR: FC<PropsType> = ({ Svg, act }) => {
  const $clr = $argClr[act];

  return (
    <div className="w-full flex justify-center">
      <Svg
        className={`${s.icon} w-[200px] h-[200px] sm:w-[250px] sm:h-[250px] md:w-[300px] md:h-[300px] lg:w-[350px] lg:h-[350px]`}
        style={{
          color: $clr,
        }}
      />
    </div>
  );
};

export default BounceIconSSR;
