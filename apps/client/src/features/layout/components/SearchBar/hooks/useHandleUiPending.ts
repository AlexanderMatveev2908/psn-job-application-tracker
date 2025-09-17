import { useEffect, useRef } from "react";
import { useSearchCtxConsumer } from "../context/hooks/useSearchCtxConsumer";
import { clearTmr } from "@/core/lib/etc";

type Params = {
  rootLoading: boolean;
};

export const useHandleUiPending = ({ rootLoading }: Params) => {
  const timerID = useRef<NodeJS.Timeout>(null);

  const {
    setPending,
    pending: { reset, submit },
  } = useSearchCtxConsumer();

  useEffect(() => {
    if (!rootLoading && [reset, submit].some(Boolean))
      timerID.current = setTimeout(() => {
        setPending({ key: submit ? "submit" : "reset", val: false });
        clearTmr(timerID);
      }, 250);

    return () => {
      clearTmr(timerID);
    };
  }, [reset, submit, rootLoading, setPending]);
};
