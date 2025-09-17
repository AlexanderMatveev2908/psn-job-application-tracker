/** @jsxImportSource @emotion/react */
"use client";

import { useState, type FC } from "react";
import FormFieldPwd from "@/common/components/forms/inputs/FormFieldPwd";
import { useFormContext, useWatch } from "react-hook-form";
import { useSyncPortal } from "@/core/hooks/etc/tooltips/useSyncPortal";
import PwdMatchTracker from "./components/PwdMatchTracker/PwdMatchTracker";
import PwdGenerator from "./components/PwdGenerator/PwdGenerator";
import { useTogglePwd } from "@/core/hooks/etc/useTogglePwd";
import { pwdFields } from "@/core/uiFactory/formFields";
import { SwapModeT } from "@/core/hooks/etc/useSwap/etc/initState";

type PropsType = {
  isCurrSwap?: boolean;
  swapMode?: SwapModeT;
};

const PairPwd: FC<PropsType> = ({
  isCurrSwap = true,
  swapMode = "swapped",
}) => {
  const [isFocus, setIsFocus] = useState(false);

  const formCtx = useFormContext();
  const { control, trigger, getFieldState, formState } = formCtx;
  const pwd = useWatch({
    control,
    name: "password",
  });

  const { handleConfPwd, handlePwdClick, isConfPwdShw, isPwdShw } =
    useTogglePwd();

  const { coords, parentRef } = useSyncPortal([swapMode]);

  const portalConf = {
    showPortal: isCurrSwap && swapMode !== "swapping",
    optDep: [swapMode],
  };

  return (
    <div className="cont__grid__md">
      <PwdMatchTracker
        {...{
          coords,
          isCurrSwap,
          swapMode,
          isFocus,
          pwd,
          isDirty:
            formState.touchedFields.password ||
            getFieldState("password", formState).isDirty,
        }}
      />

      <FormFieldPwd
        {...{
          el: pwdFields.password,
          control,
          cbChange: () => trigger("confirm_password"),
          cbFocus: () => setIsFocus(true),
          cbBlur: () => setIsFocus(false),
          isPwdShw: isPwdShw,
          handleSvgClick: handlePwdClick,
          optRef: parentRef,
          portalConf,
        }}
      />

      <PwdGenerator {...{ swapMode, isCurrSwap }} />

      <FormFieldPwd
        {...{
          el: pwdFields.confirm_password,
          control,
          cbChange: () => trigger("password"),
          isPwdShw: isConfPwdShw,
          handleSvgClick: handleConfPwd,
          portalConf,
        }}
      />
    </div>
  );
};

export default PairPwd;
