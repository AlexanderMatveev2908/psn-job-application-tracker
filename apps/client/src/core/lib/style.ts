import { RefObject } from "react";
import { uiBreaks } from "../constants/uiBreaks";

export const resp = (str: keyof typeof uiBreaks | number | undefined) =>
  !str
    ? ""
    : str in uiBreaks
    ? `@media screen and (min-width: ${
        uiBreaks[str as keyof typeof uiBreaks]
      }px)`
    : `@media screen and (min-width: ${str}px)`;

export const lockFocusCb = (
  lockFocusRef: RefObject<boolean>,
  ms: number = 600
) => {
  lockFocusRef.current = true;

  const t = setTimeout(() => {
    lockFocusRef.current = false;
    clearTimeout(t);
  }, ms);
};
