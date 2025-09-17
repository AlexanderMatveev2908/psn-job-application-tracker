import { uiBreaks } from "@/core/constants/uiBreaks";
import { isWdw } from "@/core/lib/etc";

export const getMaxBtnForSwap = () => {
  if (!isWdw()) return 1;

  const w = window.innerWidth;

  return w > uiBreaks.xl
    ? 8
    : w > uiBreaks.lg
    ? 6
    : w > uiBreaks.md
    ? 4
    : w > uiBreaks.sm
    ? 3
    : w > 450
    ? 2
    : 1;
};

export const getNumCardsForPage = () => {
  if (!isWdw()) return 1;

  const w = window.innerWidth;

  return w > uiBreaks.xl
    ? 8
    : w > uiBreaks.lg
    ? 6
    : w > uiBreaks.md
    ? 3
    : w > uiBreaks.sm
    ? 2
    : 1;
};
