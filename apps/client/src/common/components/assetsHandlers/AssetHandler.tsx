/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { motion } from "framer-motion";
import { css } from "@emotion/react";
import { resp } from "@/core/lib/style";
import { CircleAlert } from "lucide-react";
import BounceIconCSR from "../elements/bounceIcons/BounceIconCSR";

type PropsType = {
  isError: boolean;
  loaded: boolean;
};

const AssetHandler: FC<PropsType> = ({ loaded, isError }) => {
  return (
    <>
      {!loaded && !isError && (
        <motion.div
          className="absolute inset-0"
          css={css`
            background: linear-gradient(
              to right,
              var(--neutral__800) 0%,
              var(--neutral__700) 40%,
              var(--neutral__800) 80%
            );
            background-size: 200% 100%;
          `}
          animate={{
            backgroundPosition: ["200% 0%", "-200% 0%"],
          }}
          transition={{
            duration: 2,
            repeat: Infinity,
            ease: "linear",
          }}
        ></motion.div>
      )}

      {isError && (
        <div
          className="flex flex-col gap-3 justify-center items-center py-3 rounded-2xl min-h-full min-w-full h-full"
          css={css`
            border: 2px solid var(--neutral__700);
          `}
        >
          <div className="w-full flex justify-center items-center flex-col gap-2">
            <span
              className="txt__md"
              css={css`
                color: var(--red__600);
              `}
            >
              Error loading asset
            </span>
          </div>

          <BounceIconCSR
            {...{
              Svg: CircleAlert,
              $customCSS: css`
                color: var(--red__600);

                min-width: 75px;
                min-height: 75px;

                ${resp("sm")} {
                  min-width: 100px;
                  min-height: 100px;
                }
              `,
            }}
          />
        </div>
      )}
    </>
  );
};

export default AssetHandler;
