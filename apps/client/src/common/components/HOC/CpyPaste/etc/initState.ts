export const initState = { isCopied: false, x: 0 };

export type CpyPasteStateT = typeof initState;

export type CpyPasteActionsT = { type: "OPEN" } | { type: "CLOSE" };
