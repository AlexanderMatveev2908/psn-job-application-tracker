import { useCallback, useReducer, useRef } from "react";
import { searchBarInitState } from "../etc/initState";
import { searchbarReducer } from "../etc/reducer";
import {
  PayloadPaginationT,
  PayloadPendingT,
  PayloadSearchAPIT,
  PayloadSetBarT,
} from "../etc/actions";
import { genURLSearchQuery } from "@/core/lib/forms";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { TriggerApiT } from "@/common/types/api";
import { cpyObj } from "@/core/lib/dataStructure/ect";
import { FieldTxtArrT } from "@/common/types/ui";
import { FieldValues } from "react-hook-form";

export type PrevDataT<T> = {
  txtFields: FieldTxtArrT[];
} & T;

export type FreshDataArgT<T> = {
  page: number;
  limit: number;
} & PrevDataT<T>;

export const useSearchCtxProvider = <T extends FieldValues, R>() => {
  const [state, dispatchRct] = useReducer(searchbarReducer, searchBarInitState);
  const prevData = useRef<PrevDataT<T> | null>(null);

  const { wrapAPI } = useWrapAPI();

  const setBar = useCallback(
    (payload: PayloadSetBarT) => dispatchRct({ type: "SET_BAR", payload }),
    []
  );

  const setCurrFilter = useCallback(
    (payload: { val: string }) =>
      dispatchRct({ type: "SET_CURR_FILTER", payload }),
    []
  );

  const setPagination = useCallback(
    (payload: PayloadPaginationT) =>
      dispatchRct({ type: "SET_PAGINATION", payload }),
    []
  );

  const setPending = useCallback(
    (payload: PayloadPendingT) => dispatchRct({ type: "SET_PENDING", payload }),
    []
  );

  const setSearchApi = useCallback(
    (payload: PayloadSearchAPIT) => dispatchRct({ type: "SET_API", payload }),
    []
  );

  const triggerSearch = useCallback(
    async (arg: {
      freshData: FreshDataArgT<T>;
      triggerRTK: TriggerApiT<R>;
      keyPending: "submit" | "reset";
      payloadPagination?: PayloadPaginationT;
      skipCall?: boolean;
    }) => {
      const cpy = cpyObj(arg.freshData);

      // ? pagination is handled separately so does not need to be tracked
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { page, limit, ...rst } = cpy;
      prevData.current = rst as PrevDataT<T>;

      // ? is enough to send to server key value pairs, no need to send all object with useless properties for sql query
      for (const field of cpy?.txtFields ?? []) {
        (cpy as Record<string, unknown>)[field.name] = field.val;
      }

      setPending({ key: arg.keyPending, val: true });

      if (arg.payloadPagination) setPagination(arg.payloadPagination);

      setSearchApi({ key: "skipCall", val: !!arg.skipCall });

      await wrapAPI({
        cbAPI: () => arg.triggerRTK(genURLSearchQuery(cpy)),
        showToast: false,
      });
    },
    [wrapAPI, setPending, setSearchApi, setPagination]
  );

  return {
    ...state,
    setBar,
    setCurrFilter,
    setPagination,
    setPending,
    prevData,
    triggerSearch,
  };
};

export type SearchBarCtxT = ReturnType<typeof useSearchCtxProvider>;
