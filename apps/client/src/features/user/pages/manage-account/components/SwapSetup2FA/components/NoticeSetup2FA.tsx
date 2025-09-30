/** @jsxImportSource @emotion/react */
"use client";

import { UserT } from "@/features/user/types";
import { CircleAlert, CircleCheckBig } from "lucide-react";
import type { FC } from "react";
import { FaCircleQuestion } from "react-icons/fa6";

type PropsType = {
  user: UserT | null;
};

const NoticeSetup2FA: FC<PropsType> = ({ user }) => {
  const Svg = user?.use2FA
    ? CircleCheckBig
    : user?.isVerified
    ? FaCircleQuestion
    : CircleAlert;
  const $baseTwd = "w-[150px] h-[150px]";

  const msg = user?.use2FA
    ? "User has 2FA activated"
    : user?.isVerified
    ? "Setup 2FA with TOTP code"
    : "User need to confirm account before setup 2FA";

  return (
    <div className="cont__grid__lg justify-items-center">
      <Svg
        className={`${$baseTwd} ${
          user?.use2FA
            ? "text-green-600"
            : user?.isVerified
            ? "text-blue-600"
            : "text-red-600"
        }`}
      />

      <span className="w-[80%] txt__lg text-center">{msg}</span>
    </div>
  );
};

export default NoticeSetup2FA;
