/** @jsxImportSource @emotion/react */
"use client";

import { RawEventT, RawFieldPropsT } from "@/common/types/ui";
import { ReactNode, useCallback } from "react";
import {
  Controller,
  ControllerRenderProps,
  FieldValues,
  Path,
} from "react-hook-form";
import ErrField from "../../etc/ErrField";
import { useSyncPortal } from "@/core/hooks/etc/tooltips/useSyncPortal";
import Portal from "@/common/components/wrappers/portals/Portal";
import { css } from "@emotion/react";
import { isObjOk } from "@/core/lib/dataStructure/ect";

type PropsType<T extends FieldValues> = {
  children?: ReactNode;
} & RawFieldPropsT<T>;

const RawField = <T extends FieldValues>({
  el,
  cbChange,
  cbBlur,
  cbFocus,
  isDisabled,
  manualMsg,
  showLabel,
  children,
  control,
  dynamicInputT,
  optRef,
  portalConf,
  manualTestID,
}: PropsType<T>) => {
  const { coords, parentRef } = useSyncPortal(portalConf?.optDep);

  const genDefProps = useCallback(
    (field: ControllerRenderProps<T, Path<T>>) => ({
      "data-testid": manualTestID ?? el.name,
      placeholder: el.place,
      disabled: !!isDisabled,
      value: field.value ?? "",
      ref: (node: HTMLInputElement | HTMLTextAreaElement | null) => {
        field.ref(node);
        parentRef.current = node;

        if (optRef) optRef.current = node;
      },
      className: `input__txt txt__md ${
        el.type === "textarea" && "scroll__app"
      }`,

      onChange: (e: RawEventT) => {
        const {
          target: { value: v },
        } = e;

        field.onChange(v);

        cbChange?.(v);
      },
      onFocus: (e: RawEventT) => {
        const {
          target: { value: v },
        } = e;

        cbFocus?.(v);
      },
      onBlur: (e: RawEventT) => {
        const {
          target: { value: v },
        } = e;

        cbBlur?.(v);
      },
    }),
    [el, cbChange, cbFocus, cbBlur, isDisabled, optRef, parentRef, manualTestID]
  );

  return (
    <label className="cont__grid__sm">
      {showLabel && <span className="txt__lg">{el.label}</span>}

      <div className="w-full relative">
        <Controller
          name={el.name}
          control={control}
          render={({ field, fieldState }) => {
            const msg = fieldState?.error?.message ?? manualMsg;

            return (
              <>
                {el.type === "textarea" ? (
                  <textarea {...field} {...genDefProps(field)} rows={4} />
                ) : (
                  <input
                    {...field}
                    type={dynamicInputT ?? el.type}
                    {...genDefProps(field)}
                  />
                )}

                {children}

                {isObjOk(portalConf) ? (
                  portalConf!.showPortal && (
                    <Portal>
                      <ErrField
                        {...{
                          msg,
                          $ctmCSS: css`
                            top: ${coords.top}px;
                            right: ${coords.right}px;
                          `,
                        }}
                      />
                    </Portal>
                  )
                ) : (
                  <ErrField
                    {...{
                      msg,
                    }}
                  />
                )}
              </>
            );
          }}
        />
      </div>
    </label>
  );
};

export default RawField;
