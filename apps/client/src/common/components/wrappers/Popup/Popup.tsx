/** @jsxImportSource @emotion/react */
"use client";

import { useRef, type FC } from "react";
import { motion } from "framer-motion";
import { varPop } from "./uiFactory";
import { X } from "lucide-react";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { AppEventT } from "@/common/types/api";
import { parseLabelToTestID } from "@/core/lib/etc";
import BlackBg from "../../elements/BlackBg";
import BtnSvg from "../../buttons/BtnSvg";
import BtnShadow from "../../buttons/BtnShadow";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";
import { TestIDT } from "@/common/types/ui";

export type BtnWrapPopT = {
  msg?: string;
  type?: AppEventT;
  handleClick?: () => void | Promise<void>;
  isLoading?: boolean;
};

export type WrapPopPropsType = {
  isPop: boolean | null;
  setIsPop:
    | ((val: boolean | null) => void)
    | React.Dispatch<React.SetStateAction<boolean | null>>;

  children?: React.ReactNode | (() => React.ReactNode);
  allowClose?: boolean;
  propsActions?: {
    btns: [BtnWrapPopT, BtnWrapPopT];
  };
} & TestIDT;

const Popup: FC<WrapPopPropsType> = ({
  isPop,
  setIsPop,
  children,
  allowClose = true,
  propsActions,
  testID,
}) => {
  const popRef = useRef<HTMLDivElement | null>(null);

  const {
    ids: [ids],
  } = useGenIDs({ lengths: [2] });

  useMouseOut({
    ref: popRef,
    cb: () => allowClose && setIsPop(false),
  });

  return (
    <>
      <BlackBg {...{ isDark: isPop, classIndexCSS: "z__black_bg__popup" }} />

      <motion.div
        data-testid={testID}
        ref={popRef}
        className="z__popup fixed inset-0 m-auto w-[80%] max-w-[600px] h-full max-h-[600px] bg-neutral-950 p-5 rounded-2xl border-neutral-600 border-[3px]"
        initial={{
          scaleX: 0,
          scaleY: 0,
        }}
        variants={varPop}
        animate={
          typeof isPop === "boolean" ? (isPop ? "open" : "close") : undefined
        }
      >
        <div className="flex w-full max-h-full justify-end -mt-2">
          <BtnSvg
            {...{
              handleClick: () => setIsPop(false),
              isEnabled: allowClose,
              Svg: X,
              act: "ERR",
              testID: "btn__close_popup",
            }}
          />
        </div>

        <div className="h-full w-full">
          {typeof children === "function" ? children() : children}

          {propsActions && (
            <div className="w-full grid grid-cols-1 mt-[100px] gap-8">
              {ids.map((id, idx) => {
                const label =
                  propsActions.btns[idx].msg ?? (!idx ? "Cancel" : "Confirm");

                return (
                  <div
                    key={id}
                    className="justify-self-center min-w-[250px] max-w-[300px]"
                  >
                    <BtnShadow
                      {...{
                        testID: `pop__${parseLabelToTestID(label)}__btn`,
                        el: {
                          label,
                        },
                        isLoading: propsActions.btns[idx].isLoading,
                        act:
                          propsActions.btns[idx].type ?? (!idx ? "OK" : "ERR"),
                        handleClick:
                          propsActions.btns[idx].handleClick ??
                          (() => setIsPop(false)),
                      }}
                    />
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </motion.div>
    </>
  );
};

export default Popup;
