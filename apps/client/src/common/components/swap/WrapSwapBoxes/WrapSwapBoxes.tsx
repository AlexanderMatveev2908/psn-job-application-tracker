/** @jsxImportSource @emotion/react */
"use client";

import { CheckChoiceT, FormFieldCheckT, TestIDT } from "@/common/types/ui";
import { RefObject, useCallback, useEffect, useMemo, useState } from "react";
import { FieldValues, Path, useFormContext } from "react-hook-form";
import { getColsForSwap } from "./uiFactory";
import { css } from "@emotion/react";
import SwapBoxes from "./subComponents/SwapBoxes";
import BtnsSwapper from "../components/BtnsSwapper";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import ErrField from "../../forms/etc/ErrField";
import Portal from "../../wrappers/portals/Portal";
import { useSyncPortal } from "@/core/hooks/etc/tooltips/useSyncPortal";

type PropsType<T extends FieldValues> = {
  el: FormFieldCheckT<T>;
  choices: CheckChoiceT[];
} & TestIDT;

const WrapSwapBoxes = <T extends FieldValues>({
  el,
  choices,
  testID,
}: PropsType<T>) => {
  const [colsForSwap, setColsForSwap] = useState(getColsForSwap());
  const [currSwap, setCurrSwap] = useState(0);

  useEffect(() => {
    const cb = () => {
      const newColsForSwap = getColsForSwap();
      setColsForSwap(newColsForSwap);

      const maxAllowedSwap =
        Math.ceil(choices.length / (newColsForSwap * 3)) - 1;

      if (currSwap > maxAllowedSwap) setCurrSwap(maxAllowedSwap);
    };
    cb();

    window.addEventListener("resize", cb);

    return () => {
      window.removeEventListener("resize", cb);
    };
  }, [choices.length, currSwap]);

  const totSwaps = useMemo(
    () => Math.ceil(choices.length / (colsForSwap * 3)),
    [choices.length, colsForSwap]
  );

  const { ids } = useGenIDs({ lengths: [totSwaps, choices.length] });

  const { coords, parentRef } = useSyncPortal();

  const {
    formState: { errors },
    setValue,
    getValues,
  } = useFormContext();

  const handleClick = useCallback(
    (v: T[Path<T>]) => {
      if (el.type === "radio") {
        const existing = getValues(el.name) ?? "";

        setValue(el.name, existing === v ? ("" as T[Path<T>]) : v, {
          shouldValidate: true,
        });
      } else {
        const existing = getValues(el.name) ?? [];

        setValue(el.name, [...existing, v] as T[Path<T>], {
          shouldValidate: true,
        });
      }
    },
    [el, setValue, getValues]
  );

  const isCurrChoice = useCallback(
    (v: T[Path<T>]) => {
      const existing = (getValues(el.name) ?? []) as T[Path<T>];

      if (el.type === "radio") return existing === v;
      else return existing.includes(v);
    },
    [el, getValues]
  );

  return (
    <div data-testid={`wrap_swap_boxes__${testID}`} className="cont__grid__md">
      <div className="w-full flex justify-start">
        <span className="txt__lg">{el.label}</span>
      </div>

      <div className="w-full flex flex-col border-3 border-w__0 rounded-xl mx-auto max-w-[95%] overflow-hidden py-5 gap-10">
        <div
          className="relative"
          ref={parentRef as RefObject<HTMLDivElement>}
          css={css`
            display: grid;
            grid-template-columns: repeat(${totSwaps}, 100%);
            min-width: 100%;
            transition: 0.6s;
            transform: translateX(-${currSwap * 100}%);
          `}
        >
          <Portal>
            <ErrField
              {...{
                msg: errors?.[el.name]?.message as string,
                $ctmCSS: css`
                  top: ${coords.top - 25}px;
                  right: ${coords.right}px;
                `,
              }}
            />
          </Portal>

          {ids[0].map((id, idx) => {
            const boxesForSwap = colsForSwap * 3;
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const calcSlice = (arg: any[]) =>
              arg.slice(idx * boxesForSwap, (idx + 1) * boxesForSwap);

            return (
              <SwapBoxes
                key={id}
                {...{
                  choices: calcSlice(choices),
                  colsForSwap,
                  isCurr: idx === currSwap,
                  ids: calcSlice(ids[1]),
                  handleClick,
                  isCurrChoice,
                  el,
                }}
              />
            );
          })}
        </div>

        <div className="cont__grid__lg p-5">
          <BtnsSwapper
            {...{
              setCurrSwap,
              totSwaps,
              currSwap,
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default WrapSwapBoxes;
