/** @jsxImportSource @emotion/react */

"use client";

import { useMemo, type FC } from "react";
import { v4 } from "uuid";
import { easeInOut, motion } from "framer-motion";
import { css } from "@emotion/react";
import { resp } from "@/core/lib/style";
import { AppEventT } from "@/common/types/api";
import { $argClr } from "@/core/uiFactory/style";

type PropsType = {
  act?: AppEventT;
};

const SpinBtn: FC<PropsType> = ({ act = "NONE" }) => {
  const ids = useMemo(() => Array.from({ length: 4 }, () => v4()), []);

  const $clr = $argClr[act as keyof typeof $argClr];

  return (
    <div className="flex gap-5 items-center">
      {ids.map((id, i) => (
        <motion.div
          key={id}
          css={css`
            width: 25px;
            height: 25px;

            ${resp(350)} {
              width: 30px;
              height: 30px;
            }
            ${resp("sm")} {
              width: 35px;
              height: 35px;
            }

            background: ${$clr};
            box-shadow: 0 0 5px ${$clr}, 0 0 10px ${$clr}, 0 0 15px ${$clr},
              0 0 20px ${$clr}, 0 0 25px ${$clr};
            border-radius: 50%;
          `}
          transition={{
            duration: 1,
            delay: (i * 1) / ids.length,
            ease: easeInOut,
            repeat: Infinity,
          }}
          animate={{
            scale: [1, 1.25, 1],
            y: [0, 35, 0],
          }}
        ></motion.div>
      ))}
    </div>
  );
};

export default SpinBtn;
