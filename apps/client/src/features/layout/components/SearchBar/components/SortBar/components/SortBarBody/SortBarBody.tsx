/** @jsxImportSource @emotion/react */
"use client";

import { css } from "@emotion/react";
import { useCallback, type FC } from "react";
import { SorterSearchBarT } from "../../../../types";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import SortBarBodyPair, { ArgFnSorter } from "./components/SortBarBodyPair";
import { useFormContext } from "react-hook-form";

type PropsType = {
  sorters: SorterSearchBarT[];
};

const SortBarBody: FC<PropsType> = ({ sorters }) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [sorters.length] });

  const { getValues, setValue, watch } = useFormContext();

  const handleClick = useCallback(
    ({ el, val }: ArgFnSorter) => {
      const existing = getValues(el.name);

      setValue(el.name, existing === val ? "" : val, { shouldValidate: true });
    },
    [getValues, setValue]
  );

  return (
    <div
      data-testid={"sort_bar__body"}
      className="flex flex-col overflow-y-auto scroll__app min-h-0 py-6 gap-10"
      css={css`
        max-height: calc(100% - 100px);
      `}
    >
      {sorters.map((el, i) => (
        <SortBarBodyPair
          key={ids[i]}
          {...{
            el,
            handleClick,
            currVal: watch(el.name),
          }}
        />
      ))}
    </div>
  );
};

export default SortBarBody;
