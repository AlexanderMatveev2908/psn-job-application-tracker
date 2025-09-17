import { useCallback } from "react";
import { useHydration } from "./useHydration";

export const useWrapClientListener = () => {
  const { isHydrated } = useHydration();

  const wrapClientListener = useCallback(
    async (cb: (() => void) | (() => Promise<void>)) => {
      if (!isHydrated) return;

      await cb();
    },
    [isHydrated]
  );

  return {
    wrapClientListener,
  };
};
