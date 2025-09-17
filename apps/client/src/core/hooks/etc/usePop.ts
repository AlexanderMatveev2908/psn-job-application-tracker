import { useState } from "react";

export const usePop = () => {
  const [isPop, setIsPop] = useState<boolean | null>(null);

  return {
    isPop,
    setIsPop,
  };
};
