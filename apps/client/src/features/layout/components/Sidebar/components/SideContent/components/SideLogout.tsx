/** @jsxImportSource @emotion/react */
"use client";

import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { linkLogout } from "@/core/uiFactory/links";
import { useUser } from "@/features/user/hooks/useUser";
import type { FC } from "react";

type PropsType = {
  handleClick: () => void;
};

const SideLogout: FC<PropsType> = ({ handleClick }) => {
  const { isVoluntaryLoggingOut, voluntaryLogoutUser } = useUser();

  return (
    <button
      data-testid={`side_link__logout`}
      onClick={async () => {
        await voluntaryLogoutUser();
        handleClick();
      }}
      className="link__app flex items-center justify-start gap-6"
    >
      <PairTxtSvg {...{ el: linkLogout, isLoading: isVoluntaryLoggingOut }} />
    </button>
  );
};

export default SideLogout;
