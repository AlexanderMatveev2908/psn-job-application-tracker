/** @jsxImportSource @emotion/react */
"use client";

import ErrField from "@/common/components/forms/etc/ErrField";
import { PropsTypeWrapSwap } from "@/common/components/swap/components/WrapSwap";
import WrapSwapMultiForm from "@/common/components/swap/WrapMultiFormSwapper/subComponents/WrapSwapMultiForm";
import Portal from "@/common/components/wrappers/portals/Portal";
import { useSyncPortal } from "@/core/hooks/etc/tooltips/useSyncPortal";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { SwapStateT } from "@/core/hooks/etc/useSwap/etc/initState";
import { ToptFormT } from "@/core/paperwork";
import { css } from "@emotion/react";
import { RefObject, type FC } from "react";
import { Controller, UseFormReturn } from "react-hook-form";
import { useSideStuffTotpForm } from "./hooks/useSideStuffTotpForm";
import { useFocusMultiForm } from "@/core/hooks/etc/focus/useFocusMultiForm";
import { useFocus } from "@/core/hooks/etc/focus/useFocus";

type PropsType = {
  formCtx: UseFormReturn<ToptFormT>;
  handleSave: () => void;
  swapState: SwapStateT;
} & Omit<PropsTypeWrapSwap, "children">;

const TotpForm: FC<PropsType> = ({
  contentRef,
  isCurr,
  formCtx,
  handleSave,
  swapState,
}) => {
  const {
    watch,
    control,
    formState: { errors },
    setFocus,
    setValue,
    trigger,
  } = formCtx;

  useFocus("totp_code.0", { setFocus });
  useFocusMultiForm<ToptFormT>({
    keyField: "totp_code.0",
    setFocus,
    swapState,
    targetSwap: 0,
  });

  const codeForm = watch();

  const { ctrlPressed, setCtrlPressed, setCurrFocus } = useSideStuffTotpForm({
    setFocus,
    setValue,
    totp_code: codeForm.totp_code,
    trigger,
    isCurr,
  });

  const { ids } = useGenIDs({ lengths: [2, 3, 3] });

  const { coords, parentRef } = useSyncPortal([swapState]);

  return (
    <WrapSwapMultiForm
      {...{
        contentRef,
        isCurr,
        formCtx,
        handleSave,
        isLoading: formCtx.formState.isSubmitting,
        title: "Totp Code",
      }}
    >
      <div
        ref={parentRef as RefObject<HTMLDivElement>}
        className="w-fit grid grid-cols-1 items-center gap-6 sm:flex sm:justify-center justify-items-center relative mx-auto"
      >
        {Array.from({ length: 2 }).map((_, idx) => (
          <div
            key={ids[0][idx]}
            className="flex items-center justify-center gap-6"
          >
            {ids[idx + 1].map((id, innerIdx) => (
              <Controller
                key={id}
                name={`totp_code.${innerIdx + idx * 3}`}
                control={control}
                render={({ field }) => (
                  <input
                    {...field}
                    type="text"
                    data-testid={`totp_code.${innerIdx + idx * 3}`}
                    value={field.value ?? ""}
                    // eslint-disable-next-line @typescript-eslint/no-unused-vars
                    onChange={({ target: { value: v } }) => {
                      // if (v.length > 1) {
                      //   if (!v.at(-1)?.trim()) return;
                      //   field.onChange(v.at(-1));
                      //   setFocus(`totp_code.${realLength}`);
                      //   return;
                      // }
                      // if (!REG_INT.test(v)) return;
                      // field.onChange(v);
                      // setFocus(`totp_code.${realLength + 1}`);
                    }}
                    onFocus={() => {
                      setCurrFocus(innerIdx + idx * 3);
                    }}
                    onBlur={() => {
                      setCtrlPressed(false);
                      setCurrFocus(null);
                    }}
                    ref={(node: HTMLInputElement) => {
                      field.ref(node);
                    }}
                    className={`w-[50px] h-[50px] input__base px-[15px] txt__lg ${
                      ctrlPressed ? "text-black bg-w__0" : ""
                    }`}
                  />
                )}
              />
            ))}

            {isCurr && swapState.swapMode !== "swapping" && (
              <Portal>
                <ErrField
                  {...{
                    msg: errors?.totp_code?.root?.message,
                    $ctmCSS: css`
                      top: ${coords.top}px;
                      right: ${coords.right}px;
                    `,
                  }}
                />
              </Portal>
            )}
          </div>
        ))}
      </div>
    </WrapSwapMultiForm>
  );
};

export default TotpForm;
