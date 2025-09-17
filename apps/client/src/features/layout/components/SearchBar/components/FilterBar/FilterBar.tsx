/** @jsxImportSource @emotion/react */
"use client";

import { useRef, type FC } from "react";
import BlackBg from "@/common/components/elements/BlackBg";
import { css } from "@emotion/react";
import FilterBarHeader from "./components/FilterBarHeader";
import { MainBtnsSearchBarPropsType } from "../subComponents/MainBtnsSearchBar";
import FilterBarFooter from "./components/FilterBarFooter";
import FilterBarBody from "./components/FilterBarBody/FilterBarBody";
import { FilterSearchBarT } from "../../types";
import { useSearchCtxConsumer } from "../../context/hooks/useSearchCtxConsumer";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";

type PropsType = {
  filters: FilterSearchBarT[];
} & MainBtnsSearchBarPropsType;

const FilterBar: FC<PropsType> = ({ handleReset, filters }) => {
  const {
    bars: { filterBar },
    setBar,
  } = useSearchCtxConsumer();

  const filterBarRef = useRef<HTMLDivElement | null>(null);

  useMouseOut({
    ref: filterBarRef,
    cb: () => setBar({ bar: "filterBar", val: false }),
  });

  return (
    <>
      <BlackBg
        {...{
          isDark: filterBar,
          classIndexCSS: "z__black_bg__search_bar",
        }}
      />

      <div
        data-testid={"search_bar__filter_bar"}
        ref={filterBarRef}
        className="z__search_bar w-full flex flex-col fixed bottom-0 left-0 border-[3px] border-neutral-800 rounded-t-xl h-[600px] bg-neutral-950"
        css={css`
          transition: 0.4s;
          transform: translateY(${filterBar ? "0" : "100%"});
          opacity: ${filterBar ? 1 : 0.5};
          pointer-events: ${filterBar ? "all" : "none"};
        `}
      >
        <FilterBarHeader
          {...{
            setBar,
          }}
        />

        <FilterBarBody
          {...{
            filters,
          }}
        />

        <FilterBarFooter
          {...{
            handleReset,
          }}
        />
      </div>
    </>
  );
};

export default FilterBar;
