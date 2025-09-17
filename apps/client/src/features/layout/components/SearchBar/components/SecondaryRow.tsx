/** @jsxImportSource @emotion/react */
"use client";

import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { barsBtns } from "../uiFactory";
import BtnShadow from "@/common/components/buttons/BtnShadow";
import { resp } from "@/core/lib/style";
import { css } from "@emotion/react";
import { FC } from "react";
import { useSearchCtxConsumer } from "../context/hooks/useSearchCtxConsumer";

const SecondaryRow: FC = ({}) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [2] });

  const { setBar } = useSearchCtxConsumer();

  return (
    <div className="w-full grid grid-cols-1 gap-8">
      <div className="search_bar__wrap_btns">
        {barsBtns.map((btn, i) => {
          const bar = !i ? "filterBar" : "sortBar";
          return (
            <div key={ids[i]} className="search_bar__btn">
              <BtnShadow
                {...{
                  el: btn,
                  act: "INFO",
                  handleClick: () => setBar({ bar, val: true }),
                  $customLabelCSS: css`
                    display: none;

                    ${resp("md")} {
                      display: block;
                    }
                  `,
                  testID: `search_bar__btn__${bar}`,
                }}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default SecondaryRow;
