import { useEffect, useRef, useState } from "react";
import { v4 } from "uuid";

type Params = {
  lengths: number[];
};

const genV4 = (lengths: Params["lengths"]) =>
  lengths.map((num) => Array.from({ length: num }, () => v4()));

export const useGenIDs = ({ lengths }: Params) => {
  const [ids, setIDs] = useState(genV4(lengths));
  const prevLengths = useRef<number[]>([]);

  useEffect(() => {
    const shouldRerender =
      lengths.length !== prevLengths.current.length ||
      lengths.some((len, i) => len !== prevLengths.current[i]);

    if (!shouldRerender) return;

    setIDs(genV4(lengths));
    prevLengths.current = [...lengths];
  }, [lengths]);

  return {
    ids,
  };
};
