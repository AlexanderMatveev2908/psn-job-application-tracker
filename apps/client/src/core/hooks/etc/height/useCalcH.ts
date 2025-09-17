import { useEffect, useRef, useState } from "react";

export const useCalcH = () => {
  const [maxH, setMaxH] = useState(0);
  const contentRef = useRef<HTMLElement | null>(null);

  useEffect(() => {
    const el = contentRef.current;
    if (!el) return;

    const cb = () => setMaxH(el.scrollHeight);
    cb();

    const obs = new ResizeObserver(cb);
    obs.observe(el);

    return () => {
      obs.disconnect();
    };
  }, []);

  return {
    contentRef,
    maxH,
  };
};
