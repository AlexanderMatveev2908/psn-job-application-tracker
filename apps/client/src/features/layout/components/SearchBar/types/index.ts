import { CheckChoiceT, FieldCheckT } from "@/common/types/ui";
import { IconType } from "react-icons";

type SearchBarField = {
  name: string;
  label: string;
  Svg: IconType;
};

export type FilterSearchBarT = {
  type: FieldCheckT;
  options: CheckChoiceT[];
} & SearchBarField;

export type SorterSearchBarT = {} & SearchBarField;
