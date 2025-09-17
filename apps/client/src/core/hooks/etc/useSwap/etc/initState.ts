export type SwapModeT = "swapped" | "swapping" | "none";

export interface SwapStateT {
  currSwap: number;
  swapMode: SwapModeT;
}

export const initState: SwapStateT = {
  currSwap: 0,
  swapMode: "none",
};

export type PayloadStartSwapT = { swap: number };

export type SwapActionsT =
  | {
      type: "START_SWAP";
      payload: PayloadStartSwapT;
    }
  | {
      type: "END_SWAP";
      payload?: SwapModeT;
    };
