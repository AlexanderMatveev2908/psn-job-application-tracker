/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef, useState } from "react";
import { useWrapClientListener } from "../hydration/useWrapClientListener";

type Params = {
  opdDep?: any[];
};

export const useListenHeight = ({ opdDep }: Params) => {
  const contentRef = useRef<HTMLDivElement>(null);
  const [contentH, setContentH] = useState(0);

  const { wrapClientListener } = useWrapClientListener();

  useEffect(() => {
    const el = contentRef.current;
    if (!el) return;

    const cb = () => setContentH(el.offsetHeight);
    wrapClientListener(cb);

    const ro = new ResizeObserver(cb);
    ro.observe(el);

    window.addEventListener("resize", cb);
    return () => {
      ro.disconnect();
      window.removeEventListener("resize", cb);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [wrapClientListener, ...(opdDep ?? [])]);

  return {
    contentRef,
    contentH,
  };
};
