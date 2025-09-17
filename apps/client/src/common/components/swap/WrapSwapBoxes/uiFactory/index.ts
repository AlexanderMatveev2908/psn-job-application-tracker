import { uiBreaks } from "@/core/constants/uiBreaks";
import { isWdw } from "@/core/lib/etc";

{
}
export const getColsForSwap = () => {
  if (!isWdw()) return 1;

  const w = window.innerWidth;

  return w > uiBreaks.xl ? 4 : w > uiBreaks.lg ? 3 : w > uiBreaks.md ? 2 : 1;
};
