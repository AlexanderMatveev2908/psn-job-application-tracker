import type { FC } from "react";
import { v4 } from "uuid";
import s from "./SpinPage.module.css";
import { AppEventT } from "@/common/types/api";
import { $argClr } from "@/core/uiFactory/style";

type PropsType = {
  act?: AppEventT;
  $minH?: string;
};

const SpinPage: FC<PropsType> = ({ act = "NONE", $minH }) => {
  const ids = Array.from({ length: 15 }, () => v4());

  return (
    <div
      className={`w-full h-full ${
        $minH ?? "min-h-screen"
      } overflow-hidden flex justify-center items-center`}
    >
      <div className={`${s.__wrap} relative w-full`}>
        {ids.map((id, i) => (
          <div
            key={id}
            className="absolute inset-0"
            style={{
              transform: `rotate(${
                (360 / ids.length) * i
              }deg) translateY(-50%)`,
            }}
          >
            <div
              className={`${s.__dot} `}
              style={
                {
                  transform: "scale(0)",
                  opacity: 0,
                  "--delay_page": `${(1 / ids.length) * i}s`,
                  "--spin__clr": $argClr[act],
                } as React.CSSProperties
              }
            ></div>
          </div>
        ))}
      </div>
    </div>
  );
};
export default SpinPage;
