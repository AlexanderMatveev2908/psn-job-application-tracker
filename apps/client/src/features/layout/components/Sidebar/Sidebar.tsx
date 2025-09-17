/** @jsxImportSource @emotion/react */
"use client";

import { useRef, type FC } from "react";
import { headerHight } from "@/core/constants/style";
import { useDispatch, useSelector } from "react-redux";
import { getSideState, sideSlice } from "./slice";
import BlackBg from "@/common/components/elements/BlackBg";
import { motion } from "framer-motion";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";
import TxtScroll from "@/common/components/elements/txt/TxtScroll";
import { css } from "@emotion/react";
import SideContent from "./components/SideContent/SideContent";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";

const Sidebar: FC = () => {
  const sideRef = useRef(null);

  const sideState = useSelector(getSideState);
  const usState = useGetUserState();

  const dispatch = useDispatch();

  useMouseOut({
    ref: sideRef,
    cb: () => dispatch(sideSlice.actions.closeSide()),
  });
  return (
    <>
      <BlackBg
        {...{
          classIndexCSS: "z__bg_sidebar",
          isDark: sideState.isOpen,
        }}
      />

      <motion.div
        data-testid={"sidebar"}
        ref={sideRef}
        className="z__sidebar txt__clr fixed border-l-3 border-w__0 right-0 w-[80vw] sm:w-[500px] md:w-[600px] bg-neutral-950"
        css={css`
          top: ${headerHight}px;
          height: calc(100% - ${headerHight}px);
          max-height: calc(100% -${headerHight}px);
        `}
        initial={{
          x: "100%",
          opacity: 0,
        }}
        transition={{
          x: { duration: 0.4, ease: "easeInOut" },
          opacity: { duration: 0.3, ease: "linear" },
        }}
        animate={{
          x: sideState.isOpen ? "0" : "100%",
          opacity: sideState.isOpen ? 1 : 0,
        }}
      >
        <div className="w-full flex h-full flex-col">
          {usState.isUsOk && (
            <div
              data-testid={"sidebar__span_mail"}
              className="w-full min-h-[60px] h-[60px] sticky top-0 border-b-3 flex items-center justify-start px-5"
            >
              <TxtScroll
                {...{
                  txt: usState.user!.email,
                }}
              />
            </div>
          )}

          <div
            className="scroll__app overflow-y-auto flex flex-col px-5 pt-3 pb-8"
            css={css`
              height: calc(100% - 60px);
              max-height: calc(100% - 60px);
            `}
          >
            <SideContent />
          </div>
        </div>
      </motion.div>
    </>
  );
};

export default Sidebar;
