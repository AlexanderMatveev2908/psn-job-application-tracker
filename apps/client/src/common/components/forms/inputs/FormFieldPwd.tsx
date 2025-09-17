/** @jsxImportSource @emotion/react */
"use client";

import { RawFieldPropsT } from "@/common/types/ui";
import { CSSProperties } from "react";
import { FieldValues } from "react-hook-form";
import { FaLock, FaLockOpen } from "react-icons/fa6";
import RawField from "./subComponents/RawField";

type PropsType<T extends FieldValues> = {
  isPwdShw: boolean;
  handleSvgClick: () => void;
} & RawFieldPropsT<T>;

const FormFieldPwd = <T extends FieldValues>({
  el,
  control,
  cbChange,
  cbFocus,
  cbBlur,
  isDisabled,
  manualMsg,
  showLabel = true,
  isPwdShw,
  handleSvgClick,
  optRef,
  portalConf,
}: PropsType<T>) => {
  const Svg = isPwdShw ? FaLockOpen : FaLock;

  return (
    <RawField
      {...{
        el,
        control,
        cbChange,
        cbFocus,
        cbBlur,
        isDisabled,
        manualMsg,
        showLabel,
        optRef,
        dynamicInputT: isPwdShw ? "text" : "password",
        portalConf,
      }}
    >
      <button
        data-testid={`form_field_pwd__toggle_${el.name}`}
        onClick={handleSvgClick}
        type="button"
        className="btn__app absolute top-1/2 -translate-y-1/2 right-4"
        style={
          {
            "--scale__up": 1.2,
          } as CSSProperties
        }
      >
        <Svg className="svg__xxs" />
      </button>
    </RawField>
  );
};

export default FormFieldPwd;
