/** @jsxImportSource @emotion/react */
"use client";

import { css } from "@emotion/react";
import type { FC } from "react";
import FilterBarBodyLabelsCol from "./components/FilterBarBodyLabelsCol";
import FilterBarBodyValsCol from "./components/FilterBarBodyValsCol";
import { FilterSearchBarT } from "../../../../types";

type PropsType = {
  filters: FilterSearchBarT[];
};

const FilterBarBody: FC<PropsType> = ({ filters }) => {
  return (
    <div
      data-testid={"filter_bar__body"}
      className="w-full grid grid-cols-[80px_3px_1fr] md:grid-cols-[250px_3px_1fr] relative"
      css={css`
        height: 100%;
      `}
    >
      <FilterBarBodyLabelsCol {...{ filters }} />
      <div className="w-full min-h-full bg-neutral-800"></div>
      <FilterBarBodyValsCol {...{ filters }} />
    </div>
  );
};

export default FilterBarBody;
