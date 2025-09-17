/** @jsxImportSource @emotion/react */
"use client";

import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { linkLogout } from "@/core/uiFactory/links";
import { useUser } from "@/features/user/hooks/useUser";
import { useState, type FC } from "react";

type PropsType = {
  handleClick: () => void;
};

const HeaderLogout: FC<PropsType> = ({ handleClick }) => {
  const [isHover, setIsHover] = useState(false);

  const { voluntaryLogoutUser, isVoluntaryLoggingOut } = useUser();

  return (
    <button
      onClick={async () => {
        await voluntaryLogoutUser();

        handleClick();
      }}
      data-testid={`header_link__logout`}
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      className="text-neutral-300 hover:text-neutral-950 hover:bg-neutral-300 transition-all duration-300 flex items-center p-2 justify-start gap-6 enabled:hover:cursor-pointer"
    >
      <PairTxtSvg
        {...{ el: linkLogout, isLoading: isVoluntaryLoggingOut, isHover }}
      />
    </button>
  );
};

export default HeaderLogout;
