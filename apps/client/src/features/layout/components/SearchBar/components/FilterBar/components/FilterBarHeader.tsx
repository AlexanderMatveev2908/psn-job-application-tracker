/** @jsxImportSource @emotion/react */
"use client";

import BtnSvg from "@/common/components/buttons/BtnSvg";
import { X } from "lucide-react";
import type { FC } from "react";
import { PayloadSetBarT } from "../../../context/etc/actions";
import Txt from "@/common/components/elements/txt/Txt";

type PropsType = {
  setBar: (arg: PayloadSetBarT) => void;
};

const FilterBarHeader: FC<PropsType> = ({ setBar }) => {
  return (
    <div className="w-full grid grid-cols-2 border-b-2 border-neutral-800 items-center px-4 min-h-[60px]">
      <Txt {...{ txt: "Filter", $size: "2xl", $justify: "start" }} />

      <div className="justify-self-end">
        <BtnSvg
          {...{
            Svg: X,
            act: "ERR",
            handleClick: () => setBar({ bar: "filterBar", val: false }),
            testID: "search_bar__btn__close_filter_bar",
          }}
        />
      </div>
    </div>
  );
};

export default FilterBarHeader;
