/** @jsxImportSource @emotion/react */
"use client";

import { css } from "@emotion/react";
import { useState, type FC } from "react";
import InnerShadow from "./components/InnerShadow";
import { TestIDT } from "@/common/types/ui";
import WrapBtnAPI from "../../wrappers/buttons/WrapBtnAPI";

type PropsType = {
  label: string;
  type?: "submit" | "button";
  handleClick?: () => void;
  isEnabled?: boolean;
  isLoading?: boolean;
} & TestIDT;

const BtnShim: FC<PropsType> = ({
  handleClick,
  label,
  type = "button",
  isEnabled = true,
  isLoading,
  testID,
}) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <WrapBtnAPI {...{ isLoading }}>
      <button
        data-testid={testID}
        type={type}
        onClick={handleClick}
        disabled={!isEnabled}
        onMouseEnter={() => setIsHover(true)}
        onMouseLeave={() => setIsHover(false)}
        className="w-full h-full"
        css={css`
          cursor: ${isEnabled ? "pointer" : "not-allowed"};
          opacity: ${isEnabled ? 1 : 0.5};
        `}
      >
        <div
          css={css`
            padding: 10px 50px;
            border: 2px solid var(--neutral__300);
            border-radius: 15px;
            transition: 0.4s;
            position: relative;
            overflow: hidden;
            width: 100%;
            max-height: 100%;
            pointer-events: none;

            box-shadow: ${isHover && isEnabled
              ? "0 0 10px var(--white__0), 0 0 20px var(--white__0), 0 0 30px var(--white__0)"
              : "none"};
          `}
        >
          <InnerShadow {...{ isHover, isEnabled }} />

          <span className="txt__lg relative z-60">{label}</span>
        </div>
      </button>
    </WrapBtnAPI>
  );
};

export default BtnShim;
