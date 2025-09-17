/* eslint-disable @typescript-eslint/no-explicit-any */
/** @jsxImportSource @emotion/react */
"use client";

import Shim from "@/common/components/elements/Shim";
import { FormFieldTxtSearchBarT } from "@/common/types/ui";
import { useHydration } from "@/core/hooks/etc/hydration/useHydration";
import { logFormErrs } from "@/core/lib/forms";
import { css } from "@emotion/react";
import {
  ArrayPath,
  FieldValues,
  Path,
  useFieldArray,
  useFormContext,
} from "react-hook-form";
import PrimaryRow from "./components/PrimaryRow";
import SecondaryRow from "./components/SecondaryRow";
import TertiaryRow from "./components/TertiaryRow";
import { useCallback, useEffect } from "react";
import AddFieldTxtDrop from "./components/AddFieldTxtDrop";
import FilterBar from "./components/FilterBar/FilterBar";
import { FilterSearchBarT, SorterSearchBarT } from "./types";
import { useSearchCtxConsumer } from "./context/hooks/useSearchCtxConsumer";
import SortBar from "./components/SortBar/SortBar";
import { useHandleUiPending } from "./hooks/useHandleUiPending";
import { ZodObject } from "zod";
import { useDebounce } from "./hooks/useDebounce";
import { FreshDataArgT } from "./context/hooks/useSearchCtxProvider";
import { TriggerApiT } from "@/common/types/api";
import HitsCounter from "./components/HitsCounter";

type PropsType<T extends FieldValues, H extends any[]> = {
  allowedTxtFields: FormFieldTxtSearchBarT<T>[];
  resetVals: T;
  filters: FilterSearchBarT[];
  sorters: SorterSearchBarT[];
  hook: H;
  schema: ZodObject;
};

const SearchBar = <
  T extends FieldValues,
  H extends any[],
  R = ReturnType<H[0]["call"]>
>({
  allowedTxtFields,
  resetVals,
  filters,
  sorters,
  hook,
  schema,
}: PropsType<T, H>) => {
  const { isHydrated } = useHydration();

  const [triggerRTK, res] = hook;

  const { watch, control, handleSubmit, reset } = useFormContext<T>();
  const {
    setCurrFilter,
    pagination: { limit },
    triggerSearch,
  } = useSearchCtxConsumer();

  const handleSave = handleSubmit(async (data) => {
    await triggerSearch({
      freshData: { ...data, page: 0, limit } as FreshDataArgT<T>,
      triggerRTK: triggerRTK as TriggerApiT<R>,
      keyPending: "submit",
      skipCall: true,
      payloadPagination: { key: "page", val: 0 },
    });
  }, logFormErrs);

  const handleReset = useCallback(async () => {
    reset(resetVals);

    await triggerSearch({
      freshData: { ...resetVals, page: 0, limit } as FreshDataArgT<T>,
      triggerRTK: triggerRTK as TriggerApiT<R>,
      keyPending: "reset",
      skipCall: true,
      payloadPagination: { key: "page", val: 0 },
    });
  }, [reset, resetVals, limit, triggerRTK, triggerSearch]);

  useEffect(() => {
    setCurrFilter({ val: filters[0].label });
  }, [filters, setCurrFilter]);

  useHandleUiPending({
    rootLoading: res?.isLoading || res?.isFetching,
  });

  useDebounce({
    schema,
    triggerRTK: triggerRTK as TriggerApiT<R>,
  });

  const existingFields: FormFieldTxtSearchBarT<T>[] =
    watch("txtFields" as Path<T>) ?? [];

  const { remove, append } = useFieldArray({
    control,
    name: "txtFields" as ArrayPath<T>,
  });

  return !isHydrated ? (
    <Shim
      {...{
        $CSS: css`
          width: 95%;
          max-width: 1200px;
          height: 200px;
        `,
      }}
    />
  ) : (
    <div data-testid={"search_bar"} className="w-full grid grid-cols-1 gap-12">
      <form
        onSubmit={handleSave}
        className="w-full max-w-[1400px] mx-auto h-fit min-h-[200px] border-3 border-w__0 rounded-xl p-5 grid grid-cols-1 gap-8"
      >
        <div className="w-full grid grid-cols-1 gap-6 relative">
          <PrimaryRow
            {...{ existingFields, remove, control, append, allowedTxtFields }}
          />
          <AddFieldTxtDrop
            {...{
              allowedTxtFields,
              append,
              existingFields,
            }}
          />
        </div>

        <div className="w-full grid grid-cols-1 gap-8 xl:grid-cols-2">
          <SecondaryRow />

          <TertiaryRow
            {...{
              handleReset,
            }}
          />
        </div>

        <FilterBar
          {...{
            handleReset,
            filters,
          }}
        />

        <SortBar
          {...{
            sorters,
          }}
        />
      </form>

      <HitsCounter
        {...{
          res,
        }}
      />
    </div>
  );
};

export default SearchBar;
