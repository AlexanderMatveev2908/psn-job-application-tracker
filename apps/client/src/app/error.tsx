"use client";

import type { FC } from "react";
import { __cg } from "@/core/lib/log";
import BtnShadow from "@/common/components/buttons/BtnShadow";
import AppEventPage from "@/common/components/wrappers/pages/AppEventPage/AppEventPage";

type PropsType = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  error: any;
  reset: () => void;
};

const Err: FC<PropsType> = ({ error: err }: PropsType) => {
  __cg("err", err);

  return (
    <AppEventPage
      {...{
        act: "ERR",
        msg: err?.msg ?? err?.data?.msg ?? err?.message ?? "Unmown error...❌",
      }}
    >
      <div className="w-[250px]">
        <BtnShadow
          {...{
            act: "ERR",
            el: { label: "Refresh" },
            handleClick: () => location.reload(),
          }}
        />
      </div>
    </AppEventPage>
  );
};

export default Err;
