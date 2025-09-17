import { PropsTypeWrapSwap } from "@/common/components/swap/components/WrapSwap";
import { SwapStateT } from "@/core/hooks/etc/useSwap/etc/initState";

export type FormManageAccPropsType = {
  swapState: SwapStateT;
} & Omit<PropsTypeWrapSwap, "children">;
