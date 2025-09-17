import { useAnimationControls } from "framer-motion";
import { useEffect, useRef } from "react";
import { useSelector } from "react-redux";
import { getToastState } from "../slices";

export const useToastAnimation = () => {
  const toastState = useSelector(getToastState);
  const controls = useAnimationControls();

  const prevX = useRef(toastState.x);
  const wasShw = useRef(false);

  useEffect(() => {
    let cancelled = false;

    const run = async () => {
      if (!toastState.isShow) {
        wasShw.current = false;
        prevX.current = toastState.x;
        return;
      }

      if (wasShw.current && toastState.x !== prevX.current) {
        await controls.start("close");
        if (cancelled || !toastState.isShow) return;
      }

      controls.stop();
      controls.set("hidden");
      await new Promise(requestAnimationFrame);

      if (cancelled || !toastState.isShow) return;

      await controls.start("open");

      wasShw.current = true;
      prevX.current = toastState.x;
    };

    void run();

    return () => {
      cancelled = true;
    };
  }, [toastState.isShow, toastState.x, controls]);

  return { controls };
};
