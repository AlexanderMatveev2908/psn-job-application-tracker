/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import MainBtnsSearchBar, {
  MainBtnsSearchBarPropsType,
} from "./subComponents/MainBtnsSearchBar";

type PropsType = {} & MainBtnsSearchBarPropsType;

const TertiaryRow: FC<PropsType> = ({ handleReset }) => {
  return (
    <div
      data-testid={"search_bar__tertiary_row"}
      className="search_bar__wrap_btns"
    >
      <MainBtnsSearchBar
        {...{
          handleReset,
          testIDs: ["tertiary_row__search", "tertiary_row__reset"],
        }}
      />
    </div>
  );
};

export default TertiaryRow;
