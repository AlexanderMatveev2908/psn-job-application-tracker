/** @jsxImportSource @emotion/react */
"use client";

import BtnShadow from "@/common/components/buttons/BtnShadow";
import { FormFieldTxtSearchBarT } from "@/common/types/ui";
import { useMouseOut } from "@/core/hooks/etc/useMouseOut";
import { css } from "@emotion/react";
import { useMemo, useRef, useState } from "react";
import { FieldValues, Path, UseFieldArrayAppend } from "react-hook-form";
import { FaPlus } from "react-icons/fa6";
import { v4 } from "uuid";
import { useSearchCtxConsumer } from "../context/hooks/useSearchCtxConsumer";
import { cpyObj } from "@/core/lib/dataStructure/ect";
import { PrevDataT } from "../context/hooks/useSearchCtxProvider";

export type AddFieldTxtPropsType<T extends FieldValues> = {
  allowedTxtFields: FormFieldTxtSearchBarT<T>[];
  existingFields: FormFieldTxtSearchBarT<T>[];
  append: UseFieldArrayAppend<T>;
};

const AddFieldTxtDrop = <T extends FieldValues>({
  allowedTxtFields,
  append,
  existingFields,
}: AddFieldTxtPropsType<T>) => {
  const { prevData } = useSearchCtxConsumer();

  const [isShw, setIsShw] = useState(false);
  const contRef = useRef<HTMLDivElement | null>(null);

  const filtered: FormFieldTxtSearchBarT<T>[] = useMemo(
    () =>
      allowedTxtFields
        .filter(
          (el) => !new Set(existingFields.map((ex) => ex.name)).has(el.name)
        )
        .map((el) => ({
          ...el,
          id: v4(),
        })),
    [existingFields, allowedTxtFields]
  );

  useMouseOut({
    cb: () => setIsShw(false),
    ref: contRef,
  });

  return (
    !!filtered.length && (
      <div
        data-testid={"search_bar__drop_row"}
        ref={contRef}
        className="w-[75px] relative"
      >
        <BtnShadow
          {...{
            act: "INFO",
            el: {
              Svg: FaPlus,
            },
            handleClick: () => setIsShw(!isShw),
            testID: "drop_row__btn",
          }}
        />

        <div
          data-testid={"drop_row__txt_fields"}
          className="absolute w-[250px] border-2 border-w__0 rounded-xl left-0 z-10 bg-neutral-950 overflow-hidden max-h-[250px] scroll__app overflow-y-auto"
          css={css`
            top: calc(100% + 10px);
            transition: 0.4s;
            transform: translateY(${isShw ? "0%" : "120%"});
            opacity: ${isShw ? "1" : "0"};
          `}
        >
          {filtered.map((el) => (
            <div
              data-testid={`txt_fields__${el.name}`}
              key={el.id}
              onClick={() => {
                const newField = {
                  ...el,
                  val: "",
                };

                prevData.current = {
                  ...cpyObj(prevData.current),
                  txtFields: [
                    ...((prevData.current as PrevDataT<T>)?.txtFields ?? []),
                    newField,
                  ],
                };

                append(newField as T[Path<T>]);
                setIsShw(false);
              }}
              className="w-full flex justify-center py-2 hover:text-neutral-950 hover:bg-neutral-300 transition-all duration-300 cursor-pointer"
            >
              <span className="txt__md">{el.label}</span>
            </div>
          ))}
        </div>
      </div>
    )
  );
};

export default AddFieldTxtDrop;
