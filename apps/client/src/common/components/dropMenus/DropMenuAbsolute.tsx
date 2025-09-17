/** @jsxImportSource @emotion/react */
"use client";

import {
  Dispatch,
  ReactNode,
  SetStateAction,
  useRef,
  useState,
  type FC,
} from "react";
import { css, SerializedStyles } from "@emotion/react";
import { FieldTxtSvgT, TestIDT } from "@/common/types/ui";
import PairTxtSvg from "../elements/PairTxtSvg";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";

type PropsType = {
  el: FieldTxtSvgT;
  children: ({
    setIsOpen,
  }: {
    setIsOpen: Dispatch<SetStateAction<boolean>>;
  }) => ReactNode;
  isEnabled?: boolean;
  $cstmDropCSS?: SerializedStyles;
  $cstmBtnCSS?: SerializedStyles;
} & TestIDT;

const DropMenuAbsolute: FC<PropsType> = ({
  el,
  isEnabled = true,
  children,
  $cstmDropCSS,
  $cstmBtnCSS,
  testID,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropRef = useRef(null);

  useMouseOut({
    ref: dropRef,
    cb: () => setIsOpen(false),
  });

  return (
    <div ref={dropRef} className="w-full relative">
      <button
        disabled={!isEnabled}
        data-testid={testID}
        onClick={() => setIsOpen((prev) => !prev)}
        css={css`
          ${$cstmBtnCSS}
        `}
        className={`btn__app w-full cursor-pointer border-2 bd__sm ${
          isOpen
            ? "text-neutral-950 bg-neutral-200"
            : "text-neutral-300 enabled:hover:text-neutral-950"
        } enabled:hover:bg-neutral-300`}
        style={
          {
            "--scale__up": 1.2,
          } as React.CSSProperties
        }
      >
        <PairTxtSvg {...{ el }} />
      </button>

      <div
        data-testid={"drop_menu_absolute__content"}
        className="absolute w-full min-w-[300px] max-w-[350px] h-fit overflow-y-auto scroll__app bg-neutral-950 z-60 border-3 border-neutral-200 rounded-xl"
        css={css`
          ${$cstmDropCSS}
          top: calc(100% + 10px);
          transition: transform 0.4s, opacity 0.3s;
          transform: translateY(${isOpen ? "0" : "75px"});
          opacity: ${isOpen ? 1 : 0};
          pointer-events: ${isOpen ? "auto" : "none"};
        `}
      >
        <div className="w-full flex flex-col max-h-[200px] scroll__app overflow-y-auto">
          {children({
            setIsOpen,
          })}
        </div>
      </div>
    </div>
  );
};

export default DropMenuAbsolute;
