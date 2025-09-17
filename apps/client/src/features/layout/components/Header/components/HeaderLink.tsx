/** @jsxImportSource @emotion/react */
"use client";

import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { LinkAppSvgT } from "@/common/types/ui";
import { parseLabelToTestID } from "@/core/lib/etc";
import Link from "next/link";
import type { FC } from "react";

type PropsType = {
  lk: LinkAppSvgT;
  isCurrPath: boolean;
  handleClick: () => void;
};

const HeaderLink: FC<PropsType> = ({ lk, isCurrPath, handleClick }) => {
  return (
    <Link
      data-testid={`header_link__${parseLabelToTestID(lk.label)}`}
      href={lk.href}
      onClick={handleClick}
      className={`${
        isCurrPath
          ? "text-neutral-950 bg-neutral-200"
          : "text-neutral-300 hover:text-neutral-950"
      } hover:bg-neutral-300 transition-all duration-300 flex items-center p-2 justify-start gap-6`}
    >
      <PairTxtSvg {...{ el: lk }} />
    </Link>
  );
};

export default HeaderLink;
