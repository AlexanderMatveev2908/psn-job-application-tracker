/** @jsxImportSource @emotion/react */
"use client";

import { FormFieldTxtT } from "@/common/types/ui";
import { Controller, FieldValues, useFormContext } from "react-hook-form";
import ErrField from "../../etc/ErrField";
import { getDefValDatePicker } from "@/core/lib/dataStructure/formatters";

type PropsType<T extends FieldValues> = {
  el: FormFieldTxtT<T>;
};

const DatePicker = <T extends FieldValues>({ el }: PropsType<T>) => {
  const { control } = useFormContext<T>();

  return (
    <div data-testid={"wrap_swap_boxes"} className="cont__grid__md">
      <div className="w-full flex justify-start">
        <span className="txt__lg">{el.label}</span>
      </div>

      <div className="w-full max-w-[300px] relative">
        <Controller
          name={el.name}
          control={control}
          render={({ field, fieldState }) => {
            const msg = fieldState?.error?.message;

            return (
              <>
                <input
                  {...field}
                  type={el.type}
                  data-testid={el.name}
                  value={field.value ?? getDefValDatePicker()}
                  className="input__txt"
                />

                <ErrField
                  {...{
                    msg,
                  }}
                />
              </>
            );
          }}
        />
      </div>
    </div>
  );
};

export default DatePicker;
