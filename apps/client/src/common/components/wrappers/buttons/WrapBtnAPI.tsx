/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { AppEventT } from "@/common/types/api";
import { ChildrenT } from "@/common/types/ui";
import SpinBtn from "../../elements/spinners/SpinBtn";

type PropsType = {
  isLoading?: boolean;
  act?: AppEventT;
} & ChildrenT;

const WrapBtnAPI: FC<PropsType> = ({ isLoading, act = "NONE", children }) => {
  return isLoading ? (
    <div className="w-full flex justify-center transition-all duration-200">
      <SpinBtn {...{ act }} />
    </div>
  ) : (
    children
  );
};

export default WrapBtnAPI;
