/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { useSearchCtxConsumer } from "../../context/hooks/useSearchCtxConsumer";
import Popup from "@/common/components/wrappers/Popup/Popup";
import SortBarBody from "./components/SortBarBody/SortBarBody";
import { SorterSearchBarT } from "../../types";
import Txt from "@/common/components/elements/txt/Txt";

type PropsType = {
  sorters: SorterSearchBarT[];
};

const SortBar: FC<PropsType> = ({ sorters }) => {
  const {
    bars: { sortBar },
    setBar,
  } = useSearchCtxConsumer();

  return (
    <Popup
      {...{
        isPop: sortBar,
        setIsPop: (val: boolean | null) => setBar({ bar: "sortBar", val }),
        testID: "search_bar__sort_bar",
      }}
    >
      <div className="w-full h-full flex flex-col gap-4">
        <Txt {...{ txt: "Sort", $size: "2xl", $justify: "center" }} />

        <SortBarBody {...{ sorters }} />
      </div>
    </Popup>
  );
};

export default SortBar;
