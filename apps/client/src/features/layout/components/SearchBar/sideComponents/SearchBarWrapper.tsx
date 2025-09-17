/* eslint-disable @typescript-eslint/no-explicit-any */
/** @jsxImportSource @emotion/react */
"use client";

import { FieldValues, FormProvider, UseFormReturn } from "react-hook-form";
import SearchBarCtxProvider from "../context/SearchBarCtxProvider";
import PageCounter from "@/features/layout/components/SearchBar/sideComponents/PageCounter/PageCounter";
import { ReactNode } from "react";
import { ZodObject } from "zod";

type PropsType<T extends FieldValues, H extends any[]> = {
  formCtx: UseFormReturn<T>;
  hook: H;
  schema: ZodObject;
  children: (arg: { hook: H; schema: ZodObject }) => ReactNode;
};

const SearchBarWrapper = <T extends FieldValues, H extends any[]>({
  formCtx,
  children,
  hook,
  schema,
}: PropsType<T, H>) => {
  return (
    <SearchBarCtxProvider>
      <FormProvider {...formCtx}>
        {children({ hook, schema })}

        <PageCounter<T, H>
          {...{
            hook,
          }}
        />
      </FormProvider>
    </SearchBarCtxProvider>
  );
};

export default SearchBarWrapper;
