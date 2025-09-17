/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";

type PropsType = {
  title: string;
  $twdCls: "xl" | "2xl" | "3xl";
};

const Title: FC<PropsType> = ({ title, $twdCls }) => {
  const $builtCls = "txt__" + $twdCls;

  return (
    <div className="w-full flex justify-center">
      <span className={` ${$builtCls} grad__txt`}>{title}</span>
    </div>
  );
};

export default Title;
