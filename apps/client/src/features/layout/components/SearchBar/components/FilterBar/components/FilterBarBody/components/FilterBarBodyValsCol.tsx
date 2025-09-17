/** @jsxImportSource @emotion/react */
"use client";

import { useCallback, useMemo, type FC } from "react";
import FilterBarBodyWrapCol from "./subComponents/FilterBarBodyWrapCol";
import { FilterSearchBarT } from "@/features/layout/components/SearchBar/types";
import { useSearchCtxConsumer } from "@/features/layout/components/SearchBar/context/hooks/useSearchCtxConsumer";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import BoxInput from "@/common/components/forms/inputs/BoxInput";
import { useFormContext } from "react-hook-form";
import { css } from "@emotion/react";

type PropsType = {
  filters: FilterSearchBarT[];
};

const FilterBarBodyValsCol: FC<PropsType> = ({ filters }) => {
  const { currFilter } = useSearchCtxConsumer();

  const { watch, setValue, getValues } = useFormContext();

  const chosenFilter = useMemo(
    () => filters.find((el) => el.label === currFilter),
    [filters, currFilter]
  );

  const handleClick = useCallback(
    (v: string) => {
      const existing =
        getValues(chosenFilter?.name ?? "") ??
        (chosenFilter?.type === "checkbox" ? [] : "");

      setValue(
        chosenFilter?.name ?? "",
        Array.isArray(existing)
          ? existing.includes(v)
            ? existing.filter((ex) => ex !== v)
            : [...existing, v]
          : existing === v
          ? ""
          : v,
        {
          shouldValidate: true,
        }
      );
    },
    [chosenFilter, getValues, setValue]
  );

  const {
    ids: [ids],
  } = useGenIDs({ lengths: [chosenFilter?.options.length ?? 0] });

  return !chosenFilter ? null : (
    <FilterBarBodyWrapCol>
      <div
        data-testid={"body__vals"}
        className="w-full grid gap-10 justify-items-center px-6"
        css={css`
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        `}
      >
        {chosenFilter.options.map((el, i) => {
          const existing =
            watch(chosenFilter.name) ??
            (chosenFilter.type === "checkbox" ? [] : "");
          const isChosen = Array.isArray(existing)
            ? existing.some((v) => v === el.val)
            : existing === el.val;

          return (
            <div key={ids[i]} className="w-[200px]">
              <BoxInput
                {...{
                  opt: el,
                  isChosen,
                  handleClick: () => handleClick(el.val as string),
                  testID: `body__vals__${el.val}`,
                }}
              />
            </div>
          );
        })}
      </div>
    </FilterBarBodyWrapCol>
  );
};

export default FilterBarBodyValsCol;
