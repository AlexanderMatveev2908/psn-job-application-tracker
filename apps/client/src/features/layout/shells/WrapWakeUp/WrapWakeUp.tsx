/** @jsxImportSource @emotion/react */
"use client";

import { useEffect, useRef, useState, type FC } from "react";
import { useWrapClientListener } from "@/core/hooks/etc/hydration/useWrapClientListener";
import { wakeUpSliceAPI } from "./slices/api";
import { clearTmr } from "@/core/lib/etc";
import { getStorage, saveStorage } from "@/core/lib/storage";
import SpinBtn from "@/common/components/elements/spinners/SpinBtn";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import Popup from "@/common/components/wrappers/Popup/Popup";
import { isStr } from "@/core/lib/dataStructure/ect";

type PropsType = {
  children: React.ReactNode;
};

const WrapWakeUp: FC<PropsType> = ({ children }) => {
  const [isPop, setIsPop] = useState<boolean | null>(null);
  const [canGo, setCanGo] = useState(false);

  const isAwakeRef = useRef<boolean>(false);
  const timerID = useRef<NodeJS.Timeout | null>(null);

  const { wrapClientListener } = useWrapClientListener();

  const [triggerRTK] = wakeUpSliceAPI.useLazyWakeServerQuery();
  const { wrapAPI } = useWrapAPI();

  useEffect(() => {
    const listener = () => {
      const lastVal = getStorage("wake_up");

      const delta = Date.now() - +(lastVal ?? 0);
      const min = delta / 1000 / 60;

      setCanGo(true);

      if (min < 15) {
        isAwakeRef.current = true;
        return;
      }

      isAwakeRef.current = false;
      setIsPop(true);
    };

    wrapClientListener(listener);
  }, [wrapClientListener]);

  useEffect(() => {
    const listener = async () => {
      if (!canGo) return;

      let count = 0;
      while (!isAwakeRef.current) {
        await new Promise<void>((resPrm) => {
          timerID.current = setTimeout(
            async () => {
              try {
                const r = await wrapAPI({
                  cbAPI: () =>
                    triggerRTK(
                      {
                        _: Date.now(),
                      },
                      false
                    ),
                });

                if (isStr(r?.msg)) {
                  saveStorage(Date.now(), { key: "wake_up" });
                  isAwakeRef.current = true;
                  setIsPop(false);
                } else {
                  count++;
                }
              } catch {
                count++;
              }

              clearTmr(timerID);
              resPrm();
            },
            count ? 1000 : 0
          );
        });
      }
    };

    wrapClientListener(listener);

    return () => {
      clearTmr(timerID);
    };
  }, [wrapAPI, triggerRTK, canGo, wrapClientListener]);

  return (
    <div className="w-full h-full">
      <Popup
        {...{
          isPop,
          setIsPop,
          allowClose: false,
        }}
      >
        <div className="w-full h-[75%] flex flex-col items-center justify-center gap-20">
          <span className="txt__lg text-neutral-200">
            Server waking up ... 💤
          </span>

          <SpinBtn />
        </div>
      </Popup>

      {children}
    </div>
  );
};

export default WrapWakeUp;
