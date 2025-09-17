import { AppEventT } from "@/common/types/api";
import { IconType } from "react-icons";
import { FaSearch } from "react-icons/fa";
import { FaEraser, FaSort } from "react-icons/fa6";
import { IoFilterSharp } from "react-icons/io5";

export type BtnSearchBarT = {
  Svg: IconType;
  label?: string;
  act: AppEventT;
};

const searchBtn: BtnSearchBarT = {
  Svg: FaSearch,
  label: "Search",
  act: "OK",
};

const eraseBtn: BtnSearchBarT = {
  Svg: FaEraser,
  label: "Reset",
  act: "ERR",
};

export const mainBtnsSearchBar = [searchBtn, eraseBtn];

export const barsBtns = [
  {
    Svg: IoFilterSharp,
    label: "Filter",
  },
  {
    Svg: FaSort,
    label: "Sort",
  },
];
