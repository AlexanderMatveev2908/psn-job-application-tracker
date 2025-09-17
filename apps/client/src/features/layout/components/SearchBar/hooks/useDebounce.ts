import { FieldValues, useFormContext } from "react-hook-form";
import { useSearchCtxConsumer } from "../context/hooks/useSearchCtxConsumer";
import { useEffect, useRef } from "react";
import { ZodObject } from "zod";
import { isSameObj } from "@/core/lib/dataStructure/ect";
import { clearTmr } from "@/core/lib/etc";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { TriggerApiT } from "@/common/types/api";
import { getNumCardsForPage } from "../sideComponents/PageCounter/uiFactory";
import { FreshDataArgT } from "../context/hooks/useSearchCtxProvider";

type Params<R> = {
  schema: ZodObject;
  triggerRTK: TriggerApiT<R>;
};

export const useDebounce = <T extends FieldValues, R>({
  schema,
  triggerRTK,
}: Params<R>) => {
  const timerID = useRef<NodeJS.Timeout>(null);

  const { prevData, setPending, triggerSearch } = useSearchCtxConsumer();
  const { watch } = useFormContext();

  const currForm = watch();

  const { wrapAPI } = useWrapAPI();

  useEffect(() => {
    const isFormOk = schema.safeParse(currForm).success;
    if (!isFormOk || timerID.current) return;

    const isSameData = isSameObj(prevData.current, currForm);
    if (isSameData) return;

    // if (skipCall) {
    //   __cg("skip api call");
    //   prevData.current = merged;
    //   return;
    // }

    timerID.current = setTimeout(async () => {
      await triggerSearch({
        freshData: {
          ...currForm,
          page: 0,
          limit: getNumCardsForPage(),
        } as FreshDataArgT<T>,
        triggerRTK,
        keyPending: "submit",
        payloadPagination: { key: "page", val: 0 },
      });
      clearTmr(timerID);
    }, 750);

    return () => {
      clearTmr(timerID);
    };
  }, [
    currForm,
    schema,
    triggerRTK,
    wrapAPI,
    setPending,
    triggerSearch,
    prevData,
    // skipCall,
  ]);
};
