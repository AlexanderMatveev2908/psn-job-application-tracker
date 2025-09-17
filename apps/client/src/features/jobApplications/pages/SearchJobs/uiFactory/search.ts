import {
  FilterSearchBarT,
  SorterSearchBarT,
} from "@/features/layout/components/SearchBar/types";
import {
  applicationStatusChoices,
  companyNameField,
  positionNameField,
} from "../../../uiFactory";
import { IoStatsChart } from "react-icons/io5";
import { CgSandClock } from "react-icons/cg";
import { parseDevValUsFriendly } from "@/core/lib/dataStructure/formatters";

export const searchJobsFieldsTxt = [companyNameField, positionNameField];

// ? used for testing functionality scroll toggle curr filter ...etc
// export const filtersSearchJobs: FilterSearchBarT[] = Array.from(
//   { length: 14 },
//   (_, i) => ({
//     name: `status_${i}`,
//     label: `Filter ${i}`,
//     Svg: IoStatsChart,
//     type: "checkbox",
//     options: Array.from({ length: 20 }, (_, ii) => ({
//       label: `option ${ii} for filter ${i}`,
//       val: `${i}__${ii}`,
//     })),
//   })
// );

export const filtersSearchJobs: FilterSearchBarT[] = [
  {
    label: "Status",
    name: "status",
    type: "checkbox",
    Svg: IoStatsChart,
    options: applicationStatusChoices,
  },
];

export const sortersSearchJobs: SorterSearchBarT[] = [
  {
    name: "created_at",
  },
  {
    name: "updated_at",
  },
  {
    name: "applied_at",
  },
].map((el) => ({
  name: el.name + "_sort",
  label: parseDevValUsFriendly(el.name, { titleCase: true }),
  Svg: CgSandClock,
}));
