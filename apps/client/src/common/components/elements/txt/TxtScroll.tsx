/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";

type PropsType = {
  txt: string;
  CSS?: string;
};

const TxtScroll: FC<PropsType> = ({ txt, CSS }) => {
  return (
    <div className="scroll__app flex w-full max-w-full h-fit overflow-x-auto pb-2 mt-2">
      <span className={`text-nowrap ${CSS ?? "txt__md"}`}>{txt}</span>
    </div>
  );
};

export default TxtScroll;
