/** @jsxImportSource @emotion/react */
"use client";

import { AppEventT } from "@/common/types/api";
import { css } from "@emotion/react";
import { FC } from "react";
import { FaCloud, FaDatabase, FaGear, FaServer } from "react-icons/fa6";
import { IoGitNetwork } from "react-icons/io5";
import { LiaCookieSolid } from "react-icons/lia";
import { $argClr } from "@/core/uiFactory/style";
import { BtnIconStyled } from "./Styled";
type PropsType = {
  label: string;
  type?: "button" | "submit";
  handleClick?: () => void;
  isEnabled?: boolean;
  isLoading?: boolean;
  act?: AppEventT;
};

const BtnIcons: FC<PropsType> = ({
  isEnabled,
  label,
  type = "button",
  handleClick,
  act = "NONE",
}) => {
  const $clr = $argClr[act];

  return (
    <BtnIconStyled
      {...{ $clr }}
      onClick={handleClick}
      type={type}
      disabled={isEnabled!}
      className="btn_icons btn__app w-full transition-all duration-500 relative"
    >
      <div
        className="relative w-full rounded-xl"
        css={css`
          overflow: hidden;
        `}
      >
        <div className="btn_icons__content relative">
          <span className={`relative z-40 txt__lg`}>{label}</span>
        </div>

        <span className="btn_icons__ref_0"></span>
        <span className="btn_icons__ref_1"></span>
      </div>

      <span className="btn_icons__shadow absolute inset-0 -z-10"></span>

      <FaDatabase className="btn_icons__svg_0" />
      <FaGear className="btn_icons__svg_1" />
      <FaCloud className="btn_icons__svg_2" />
      <FaServer className="btn_icons__svg_3" />
      <IoGitNetwork className="btn_icons__svg_4" />
      <LiaCookieSolid className="btn_icons__svg_5" />
    </BtnIconStyled>
  );
};
export default BtnIcons;
