import { useEffect, useRef } from "react";
import { useWrapClientListener } from "./useWrapClientListener";

type Params = {
  cb: (() => Promise<void>) | (() => void);
};

export const useRunOnHydrate = ({ cb }: Params) => {
  const hasRun = useRef<boolean>(false);

  const { wrapClientListener } = useWrapClientListener();

  useEffect(() => {
    const runner = async () => {
      if (hasRun.current) return;
      hasRun.current = true;

      await cb();
    };

    wrapClientListener(runner);
  }, [wrapClientListener, cb]);
};
