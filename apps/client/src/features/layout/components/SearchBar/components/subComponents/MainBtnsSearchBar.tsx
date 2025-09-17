/** @jsxImportSource @emotion/react */
"use client";

import { mainBtnsSearchBar } from "../../uiFactory";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import BtnShadow from "@/common/components/buttons/BtnShadow";
import { resp } from "@/core/lib/style";
import { css } from "@emotion/react";
import { FC } from "react";
import { useSearchCtxConsumer } from "../../context/hooks/useSearchCtxConsumer";

export type MainBtnsSearchBarPropsType = {
  handleReset: () => void;
};

type PropsType = {
  testIDs: string[];
} & MainBtnsSearchBarPropsType;

const MainBtnsSearchBar: FC<PropsType> = ({ handleReset, testIDs }) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [2] });

  const { pending } = useSearchCtxConsumer();

  return mainBtnsSearchBar.map((btn, i) => (
    <div key={ids[i]} className="search_bar__btn">
      <BtnShadow
        {...{
          act: btn.act,
          el: {
            label: btn.label,
            Svg: btn.Svg,
          },
          handleClick: btn.act === "OK" ? () => null : handleReset,
          type: btn.act === "OK" ? "submit" : "button",
          $customLabelCSS: css`
            display: none;
            ${resp("md")} {
              display: block;
            }
          `,
          testID: testIDs[i],
          isTxtLoading: btn.act === "OK" ? pending.submit : pending.reset,
        }}
      />
    </div>
  ));
};

export default MainBtnsSearchBar;
