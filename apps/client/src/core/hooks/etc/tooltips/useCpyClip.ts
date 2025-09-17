import { clearTmr } from "@/core/lib/etc";
import { useEffect, useRef, useState } from "react";

export const useCpyClip = () => {
  const [isCopied, setIsCopied] = useState(false);
  const timerID = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    clearTmr(timerID);

    if (isCopied)
      timerID.current = setTimeout(() => {
        setIsCopied(false);
        clearTmr(timerID);
      }, 1500);

    return () => {
      clearTmr(timerID);
    };
  }, [isCopied]);

  return {
    isCopied,
    setIsCopied,
  };
};
