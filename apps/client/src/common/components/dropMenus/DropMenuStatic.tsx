/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT, FieldTxtSvgT, TestIDT } from "@/common/types/ui";
import { useEffect, useRef, useState, type FC } from "react";
import { css } from "@emotion/react";
import { FaChevronDown } from "react-icons/fa6";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";
import PairTxtSvg from "../elements/PairTxtSvg";

type PropsType = {
  el: FieldTxtSvgT;
} & ChildrenT &
  TestIDT;

const DropMenuStatic: FC<PropsType> = ({ el, children }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropRef = useRef(null);

  const innerRef = useRef<HTMLDivElement>(null);
  const [contentH, setContentH] = useState(0);

  useMouseOut({
    ref: dropRef,
    cb: () => setIsOpen(false),
  });

  useEffect(() => {
    const el = innerRef.current;
    if (!el) return;

    const cb = () => setContentH(el.scrollHeight + 10);
    cb();
  }, [isOpen, children]);

  return (
    <div
      ref={dropRef}
      className={`${
        isOpen ? "gap-4" : "gap-0"
      } w-full flex flex-col translate-0 duration-300`}
    >
      <div
        data-testid={"drop_menu_static__btn_toggle"}
        role="button"
        onClick={() => setIsOpen((prev) => !prev)}
        className={`${
          isOpen
            ? "text-neutral-950 bg-neutral-200"
            : "text-neutral-300 hover:text-neutral-950"
        } flex items-center justify-between transition-all duration-300 hover:bg-neutral-300 cursor-pointer rounded-xl px-4 py-2 -ml-4`}
        css={css`
          width: calc(100% + 1rem);
        `}
      >
        <PairTxtSvg {...{ el }} />

        <FaChevronDown
          className="svg__sm"
          css={css`
            transition: 0.3s transform;
            transform: rotate(${isOpen ? 180 : 0}deg);
          `}
        />
      </div>

      <div
        className="flex w-full flex-col overflow-hidden"
        css={css`
          transition: opacity 0.3s, max-height 0.4s;
          max-height: ${isOpen ? contentH : 0}px;
          height: ${contentH}px;
          opacity: ${isOpen ? 1 : 0};
          pointer-events: ${isOpen ? "auto" : "none"};
        `}
      >
        <div
          onClick={() => setIsOpen(false)}
          ref={innerRef}
          className="w-full flex flex-col gap-4 h-full"
        >
          {children}
        </div>
      </div>
    </div>
  );
};

export default DropMenuStatic;
