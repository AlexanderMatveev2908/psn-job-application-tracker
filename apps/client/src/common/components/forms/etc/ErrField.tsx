/** @jsxImportSource @emotion/react */
"use client";

import { useEffect, useState, type FC } from "react";
import Tooltip from "../../tooltips/Tooltip/Tooltip";
import { SerializedStyles } from "@emotion/react";
import { isStr } from "@/core/lib/dataStructure/ect";

type PropsType = {
  msg?: string | null;
  $ctmCSS?: SerializedStyles;
};

const ErrField: FC<PropsType> = ({ msg, $ctmCSS }) => {
  const [prevErr, setPrevErr] = useState<string | null>(null);

  useEffect(() => {
    if (isStr(msg) && (!isStr(prevErr) || msg !== prevErr))
      setPrevErr(msg as string);
  }, [prevErr, msg]);

  return (
    <Tooltip
      {...{
        isHover: isStr(msg),
        act: "ERR",
        txt: prevErr,
        $ctmCSS,
      }}
    />
  );
};

export default ErrField;
