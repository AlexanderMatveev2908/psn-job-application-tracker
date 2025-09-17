import { useEffect } from "react";
import { useWrapClientListener } from "../hydration/useWrapClientListener";
import { usePathname } from "next/navigation";

export const useScroll = () => {
  const { wrapClientListener } = useWrapClientListener();
  const p = usePathname();

  useEffect(() => {
    const cb = () =>
      setTimeout(() => {
        window.scrollTo({
          top: 0,
          behavior: "smooth",
        });
      }, 250);

    wrapClientListener(cb);
  }, [wrapClientListener, p]);
};
