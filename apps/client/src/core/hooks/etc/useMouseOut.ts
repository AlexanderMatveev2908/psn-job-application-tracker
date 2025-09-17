import { RefObject, useEffect } from "react";

type Params = {
  ref: RefObject<HTMLElement | null>;
  cb: () => void;
};

export const useMouseOut = ({ ref, cb }: Params) => {
  useEffect(() => {
    const handleMouse = (e: MouseEvent) => {
      if (!ref.current) return;

      if (!ref.current.contains(e.target as Node)) cb();
    };

    document.addEventListener("mousedown", handleMouse);

    return () => {
      document.removeEventListener("mousedown", handleMouse);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
};
