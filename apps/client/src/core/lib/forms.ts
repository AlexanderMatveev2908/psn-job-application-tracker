/* eslint-disable @typescript-eslint/no-explicit-any */
import { FieldErrors, FieldValues, Path } from "react-hook-form";
import { __cg } from "./log";
import { isObjOk, isStr } from "./dataStructure/ect";
import { skipUselessFalsy } from "./etc";

export const swapOnErr = <T extends FieldValues>({
  errs,
  kwargs,
}: {
  kwargs: Path<T>[][];
  errs: FieldErrors<T>;
}): { i: number; field: Path<T> } | undefined => {
  let i = 0;

  while (i < kwargs.length) {
    const curr = kwargs[i];

    for (const field of curr) {
      if (errs?.[field]?.message) {
        return { i, field };
      }
    }

    i++;
  }
};

export const logFormErrs = (errs: any) => {
  __cg("errs", errs);

  return errs;
};

export const logFormData = (formData: FormData) => {
  for (const [k, v] of formData.entries()) {
    console.log(`🔑 ${k} => 💎 ${v}`);
  }
};

export const genFormData = (
  obj: any,
  formData: FormData = new FormData(),
  prefix = ""
): FormData => {
  for (const [k, v] of Object.entries(obj)) {
    if (skipUselessFalsy(v)) continue;

    const key = prefix ? `${prefix}[${k}]` : k;

    if (Array.isArray(v)) {
      const arrayKey = key + "[]";
      for (const vv of v) {
        if (isObjOk(vv)) {
          genFormData(vv, formData, arrayKey);
        } else {
          formData.append(arrayKey, isStr(vv) ? vv : vv + "");
        }
      }
    } else if (isObjOk(v)) {
      genFormData(v, formData, key);
    } else {
      formData.append(key, (isStr(v) ? v : v + "") as string);
    }
  }

  return formData;
};

export const genURLSearchQuery = <T>(obj: T): string => {
  const params = new URLSearchParams();

  for (const [k, v] of Object.entries(obj as Record<string, unknown>)) {
    if (skipUselessFalsy(v)) continue;

    if (Array.isArray(v)) {
      const arrayKey = `${k}[]`;
      for (const item of v) {
        params.append(arrayKey, String(item));
      }
    } else {
      params.append(k, String(v));
    }
  }

  return params.toString();
};
