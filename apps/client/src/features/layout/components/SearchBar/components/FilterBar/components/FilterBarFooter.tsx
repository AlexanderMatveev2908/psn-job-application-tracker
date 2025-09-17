/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import MainBtnsSearchBar, {
  MainBtnsSearchBarPropsType,
} from "../../subComponents/MainBtnsSearchBar";

type PropsType = {} & MainBtnsSearchBarPropsType;

const FilterBarFooter: FC<PropsType> = ({ handleReset }) => {
  return (
    <div
      data-testid={"filter_bar__footer"}
      className="w-full absolute bottom-0 left-0 h-[80px] grid grid-cols-2 gap-10 justify-items-center items-center bg-neutral-950 border-t-[3px] border-neutral-800 z-10"
    >
      <MainBtnsSearchBar
        {...{
          handleReset,
          testIDs: ["footer__search", "footer__reset"],
        }}
      />
    </div>
  );
};

export default FilterBarFooter;
