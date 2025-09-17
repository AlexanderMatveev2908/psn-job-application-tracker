/** @jsxImportSource @emotion/react */
"use client";

import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { LinkAppSvgT } from "@/common/types/ui";
import Link from "next/link";
import type { FC } from "react";

type PropsType = {
  lk: LinkAppSvgT;
  isCurrPath: boolean;
  handleClick: () => void;
};

const SideLink: FC<PropsType> = ({ lk, isCurrPath, handleClick }) => {
  return (
    <Link
      href={lk.href}
      onClick={handleClick}
      data-testid={`side_link__${lk.label.toLowerCase()}`}
      className={`link__app ${
        isCurrPath && "link__curr"
      } flex items-center justify-start gap-6`}
    >
      <PairTxtSvg {...{ el: lk }} />
    </Link>
  );
};

export default SideLink;
