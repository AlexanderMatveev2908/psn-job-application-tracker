/** @jsxImportSource @emotion/react */
"use client";

import { useEffect, type FC } from "react";
import { easeInOut, motion, useAnimationControls } from "framer-motion";
import { CoordsTooltipT } from "@/core/hooks/etc/tooltips/useSyncPortal";
import { css } from "@emotion/react";
import Portal from "../../../wrappers/portals/Portal";

type PropsType = {
  isCopied: boolean;
  x: number;
  coords: CoordsTooltipT;
};

const variants = {
  hidden: {
    scale: 0,
    opacity: 0,
  },
  visible: {
    scale: [0, 1.4, 0.8, 1.1, 0.9, 1, 1],
    opacity: [0, 1, 1, 1, 1, 1, 0],
    transition: {
      duration: 1.6,
      times: [0, 0.1, 0.2, 0.3, 0.4, 0.5, 1],
      ease: easeInOut,
    },
  },
};

const CpyClip: FC<PropsType> = ({ isCopied, x, coords }) => {
  const controls = useAnimationControls();

  useEffect(() => {
    let cancelled = false;

    const run = async () => {
      if (!isCopied) {
        await controls.start("hidden");
        return;
      }

      controls.stop();
      controls.set("hidden");

      await new Promise(requestAnimationFrame);
      if (cancelled) return;

      await controls.start("visible");
    };

    void run();

    return () => {
      cancelled = true;
    };
  }, [x, isCopied, controls]);

  return (
    <Portal>
      <motion.div
        css={css`
          left: ${coords.left - 75 / 2}px;
          top: ${coords.top - coords.height - 10}px;
          width: ${coords.width + 75}px;
        `}
        initial="hidden"
        animate={controls}
        variants={variants}
        className="absolute py-2 px-4 border-2 border-neutral-600 rounded-xl flex justify-center items-center pointer-events-none z-60 bg-neutral-950"
      >
        <span className="txt__sm">Copied to clipboard</span>
      </motion.div>
    </Portal>
  );
};

export default CpyClip;
