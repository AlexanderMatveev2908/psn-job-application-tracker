"use client";

import type { FC } from "react";

type PropsType = {
  txt: string;
  CSS?: string;
  lines?: number;
};

const TxtClamp: FC<PropsType> = ({ txt, CSS, lines = 2 }) => {
  return (
    <div className="w-fit max-w-full">
      <span
        className={`clamp__txt w-fit ${CSS ?? "txt__md"}`}
        style={{
          WebkitLineClamp: lines,
        }}
      >
        {txt}
      </span>
    </div>
  );
};

export default TxtClamp;
