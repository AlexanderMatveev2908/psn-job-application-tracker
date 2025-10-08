/* eslint-disable @typescript-eslint/no-explicit-any */
/** @jsxImportSource @emotion/react */
"use client";

import { PartyPopper } from "lucide-react";

type PropsType<R extends Record<string, any>> = {
  res: R;
};

const HitsCounter = <R extends Record<string, any>>({ res }: PropsType<R>) => {
  const {
    data: { nHits } = {},
    isLoading,
    isFetching,
    isUninitialized,
  } = res ?? {};

  const isPending = isLoading || isFetching || isUninitialized;

  return (
    !isPending && (
      <div className="w-full flex items-center gap-8">
        {!!nHits && <PartyPopper className="svg__md" />}

        <div className="w-full flex gap-6 items-center">
          <span data-testid={"search_bar__n_hits"} className="txt__2xl">
            {nHits}
          </span>

          <span className="txt__xl">Result{nHits !== 1 && "s"}</span>
        </div>
      </div>
    )
  );
};

export default HitsCounter;
