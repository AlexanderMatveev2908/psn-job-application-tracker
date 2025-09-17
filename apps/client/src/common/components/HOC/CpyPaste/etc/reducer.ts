// atc/reducer.ts
import { __cg } from "@/core/lib/log";
import { CpyPasteActionsT, CpyPasteStateT } from "./initState";

export const reducer = (
  s: CpyPasteStateT,
  a: CpyPasteActionsT
): CpyPasteStateT => {
  switch (a.type) {
    case "OPEN":
      return { isCopied: true, x: s.x + 1 };
    case "CLOSE":
      return { ...s, isCopied: false };
    default:
      __cg("invalid action", a);
      throw new Error("Invalid action 😡");
  }
};
