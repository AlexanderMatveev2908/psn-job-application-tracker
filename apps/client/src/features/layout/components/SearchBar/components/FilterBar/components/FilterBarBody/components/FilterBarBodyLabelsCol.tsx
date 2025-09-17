/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import FilterBarBodyWrapCol from "./subComponents/FilterBarBodyWrapCol";
import { FilterSearchBarT } from "@/features/layout/components/SearchBar/types";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { useSearchCtxConsumer } from "@/features/layout/components/SearchBar/context/hooks/useSearchCtxConsumer";

type PropsType = {
  filters: FilterSearchBarT[];
};

const FilterBarBodyLabelsCol: FC<PropsType> = ({ filters }) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [filters.length] });

  const { currFilter, setCurrFilter } = useSearchCtxConsumer();

  return (
    <FilterBarBodyWrapCol>
      <div data-testid={"body__labels"} className="w-full grid grid-cols-1">
        {filters.map((el, i) => (
          <div
            onClick={() => setCurrFilter({ val: el.label })}
            key={ids[i]}
            className={`flex justify-center items-center gap-6 hover:text-neutral-950 hover:bg-w__0 transition-all duration-300 py-3 cursor-pointer rounded-xl ${
              currFilter === el.label ? "bg-w__0 text-neutral-950" : ""
            }`}
          >
            <el.Svg className="svg__sm" />

            <span className="hidden md:block txt__md">{el.label}</span>
          </div>
        ))}
      </div>
    </FilterBarBodyWrapCol>
  );
};

export default FilterBarBodyLabelsCol;
