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
  const Svg = user?.use_2FA
    ? CircleCheckBig
    : user?.is_verified
    ? FaCircleQuestion
    : CircleAlert;
  const $baseTwd = "w-[150px] h-[150px]";

  const msg = user?.use_2FA
    ? "User has 2FA activated"
    : user?.is_verified
    ? "Setup 2FA with TOTP code"
    : "User need to confirm account before setup 2FA";

  return (
    <div className="cont__grid__lg justify-items-center">
      <Svg
        className={`${$baseTwd} ${
          user?.use_2FA
            ? "text-green-600"
            : user?.is_verified
            ? "text-blue-600"
            : "text-red-600"
        }`}
      />

      <span className="w-[80%] txt__lg text-center">{msg}</span>
    </div>
  );
};

export default NoticeSetup2FA;
