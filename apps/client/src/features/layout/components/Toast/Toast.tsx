/** @jsxImportSource @emotion/react */
"use client";

import { getToastState } from "./slices";
import { $argClr } from "@/core/uiFactory/style";
import { AnimatePresence, motion } from "framer-motion";
import { css } from "@emotion/react";
import { resp } from "@/core/lib/style";
import { varToast } from "./uiFactory";
import { X } from "lucide-react";
import { useToastStages } from "./hooks/useToastStages";
import { useDispatch, useSelector } from "react-redux";
import { FC } from "react";
import BtnSvg from "@/common/components/buttons/BtnSvg";
import { useToastAnimation } from "./hooks/useToastAnimation";

const Toast: FC = () => {
  const toastState = useSelector(getToastState);
  const dispatch = useDispatch();

  const { clickClose } = useToastStages({ dispatch, toastState });

  const clr = $argClr[toastState.toast.type ?? $argClr.NONE];

  const { controls } = useToastAnimation();

  const durSec = 5;

  return (
    <AnimatePresence>
      {toastState.isShow && (
        <motion.div
          data-testid="toast"
          className="z__toast fixed top-5 right-5 pb-6 px-5 rounded-2xl bg-[#000] grid grid-cols-1 gap-3 overflow-hidden"
          css={css`
            width: 90%;
            border: 3px solid ${clr};
            ${resp(400)} {
              width: 400px;
            }
            ${resp(600)} {
              width: 500px;
            }
            ${resp(800)} {
              width: 600px;
            }
          `}
          variants={varToast}
          initial="hidden"
          animate={controls}
          exit="close"
        >
          <div className="w-full flex justify-between items-center pt-2">
            <span
              className="txt__2xl"
              css={css`
                color: ${clr};
              `}
            >
              {toastState.toast.type?.toUpperCase()}
            </span>
            <BtnSvg
              {...{
                handleClick: clickClose,
                act: "ERR",
                Svg: X,
                testID: "toast__close_btn",
              }}
            />
          </div>

          <div className="w-full flex justify-center">
            <span className="txt__lg text-neutral-200">
              {toastState.toast.msg.slice(0, 200)}
            </span>
          </div>

          <motion.div
            key={toastState.x}
            className="w-full absolute bottom-0 left-0 h-[7.5px] rounded-2xl"
            css={css`
              background: ${clr};
            `}
            initial={{ width: "100%" }}
            animate={{ width: 0 }}
            transition={{ duration: durSec, ease: "linear" }}
          />
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default Toast;
