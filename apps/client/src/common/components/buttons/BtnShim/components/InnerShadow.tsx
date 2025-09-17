/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { easeInOut, motion } from "framer-motion";
import { css } from "@emotion/react";

type PropsType = {
  isHover?: boolean;
  isEnabled?: boolean;
};

const InnerShadow: FC<PropsType> = ({ isHover, isEnabled }) => {
  return (
    <motion.div
      css={css`
        position: absolute;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        pointer-events: none;

        .shadow_lucide_0,
        .shadow_lucide_1,
        .shadow_lucide_2,
        .shadow_lucide_3 {
          position: absolute;
          height: 100%;
          top: 0;
          transform: skew(25deg);

          opacity: 0.6;
          background: linear-gradient(
            to right,
            transparent 0%,
            var(--white__0) 50%,
            transparent 100%
          );
          filter: blur(2.5px);
          mix-blend-mode: overlay;
        }
        .shadow_lucide_0,
        .shadow_lucide_1 {
          width: 30px;
        }

        .shadow_lucide_0 {
          left: 20%;
        }
        .shadow_lucide_1 {
          left: 70%;
        }

        .shadow_lucide_2 {
          width: 80px;
          left: -120%;
        }
        .shadow_lucide_3 {
          width: 40px;
          left: -50%;
        }
      `}
      style={{
        willChange: "transform",
      }}
      initial={{
        transform: "translateX(-100%)",
      }}
      transition={{
        duration: 0.3,
        ease: easeInOut,
      }}
      animate={{
        transform:
          isHover && isEnabled ? "translateX(120%)" : `translateX(-100%)`,
      }}
    >
      <div className="shadow_lucide_0"></div>
      <div className="shadow_lucide_1"></div>

      <div className="shadow_lucide_2"></div>
      <div className="shadow_lucide_3"></div>
    </motion.div>
  );
};

export default InnerShadow;
