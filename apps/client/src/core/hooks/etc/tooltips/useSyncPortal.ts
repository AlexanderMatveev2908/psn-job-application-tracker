/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useRef, useState } from "react";
import { useHydration } from "../hydration/useHydration";

export type CoordsT = "top" | "left" | "right" | "bottom" | "width" | "height";
export type CoordsTooltipT = Record<CoordsT, number>;

export const useSyncPortal = (optDep?: any[]) => {
  const parentRef = useRef<
    HTMLElement | HTMLDivElement | HTMLButtonElement | HTMLAnchorElement | null
  >(null);
  const [coords, setCoords] = useState<CoordsTooltipT>({
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    width: 0,
    height: 0,
  });

  const { isHydrated } = useHydration();

  useEffect(() => {
    const el = parentRef.current;
    if (!el || !isHydrated) return;

    const cb = () => {
      const { top, left, right, bottom, width, height } =
        el.getBoundingClientRect();

      setCoords({
        top: top,
        left,
        right: window.innerWidth - right,
        bottom: window.innerHeight - bottom,
        width,
        height,
      });
    };

    cb();

    const ro = new ResizeObserver(cb);
    ro.observe(el);

    window.addEventListener("resize", cb);
    window.addEventListener("scroll", cb);

    return () => {
      ro.disconnect();
      window.removeEventListener("resize", cb);
      window.removeEventListener("scroll", cb);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isHydrated, ...(optDep ?? [])]);

  return {
    coords,
    parentRef,
  };
};
