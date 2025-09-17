/** @jsxImportSource @emotion/react */
"use client";

import { type FC } from "react";
import { registerSwap_0, termsField } from "../uiFactory/register";
import { useFormContext, useWatch } from "react-hook-form";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import FormFieldTxt from "@/common/components/forms/inputs/FormFieldTxt";
import { RegisterFormT } from "../paperwork";
import { useListenHeight } from "@/core/hooks/etc/height/useListenHeight";
import FormFieldCheck from "@/common/components/forms/inputs/FormFieldCheck/FormFieldCheck";
import { SwapStateT } from "@/core/hooks/etc/useSwap/etc/initState";
import PairPwd from "@/common/components/HOC/PairPwd/PairPwd";
import WrapSwapper from "@/common/components/swap/WrapSwapper";
import WrapSwap from "@/common/components/swap/components/WrapSwap";

type PropsType = {
  swapState: SwapStateT;
};

const BodyFormRegister: FC<PropsType> = ({ swapState }) => {
  const { currSwap, swapMode } = swapState;
  const { contentRef, contentH } = useListenHeight({
    opdDep: [currSwap],
  });

  const {
    control,
    formState: { errors },
    setValue,
  } = useFormContext<RegisterFormT>();
  const isChecked = useWatch<RegisterFormT>({
    control,
    name: "terms",
  });

  const { ids } = useGenIDs({
    lengths: [registerSwap_0.length, registerSwap_0.length],
  });

  return (
    <WrapSwapper
      {...{
        contentH,
        currSwap,
        totSwaps: 2,
      }}
    >
      <WrapSwap
        {...{
          isCurr: currSwap === 0,
          contentRef,
        }}
      >
        {registerSwap_0.map((el, i) => (
          <FormFieldTxt
            key={ids[0][i]}
            {...{
              el,
              control,
              portalConf: {
                showPortal: !currSwap && swapMode !== "swapping",
                optDep: [currSwap, swapMode],
              },
            }}
          />
        ))}
      </WrapSwap>

      <WrapSwap
        {...{
          isCurr: currSwap === 1,
          contentRef,
        }}
      >
        <PairPwd
          {...{
            isCurrSwap: currSwap === 1,
            swapMode,
          }}
        />

        <FormFieldCheck
          {...{
            testID: "body__form_terms",
            el: termsField,
            errors,
            isChecked: isChecked as boolean,
            setValue,
            showLabel: false,
            optTxt: "I accept terms & conditions",
          }}
        />
      </WrapSwap>
    </WrapSwapper>
  );
};

export default BodyFormRegister;
