import { FieldValues, Path, UseFormSetFocus } from "react-hook-form";
import { SwapStateT } from "../useSwap/etc/initState";
import { useEffect } from "react";

type Params<T extends FieldValues> = {
  swapState: SwapStateT;
  setFocus: UseFormSetFocus<T>;
  targetSwap: number;
  keyField: Path<T>;
};

export const useFocusMultiForm = <T extends FieldValues>({
  setFocus,
  swapState,
  targetSwap,
  keyField,
}: Params<T>) => {
  useEffect(() => {
    if (swapState.currSwap === targetSwap && swapState.swapMode !== "swapping")
      setTimeout(() => setFocus(keyField), 0);
  }, [setFocus, keyField, swapState, targetSwap]);
};
