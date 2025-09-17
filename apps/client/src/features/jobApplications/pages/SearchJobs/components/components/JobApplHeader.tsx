/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { JobApplicationT } from "@/features/jobApplications/types";
import { genPairsMainCardInfo } from "../../uiFactory/cards";

type PropsType = {
  job: JobApplicationT;
};

const JobApplHeader: FC<PropsType> = ({ job }) => {
  const { ids } = useGenIDs({ lengths: [4] });

  return genPairsMainCardInfo(job).map((pair, i) => (
    <PairTxtSvg
      key={ids[0][i]}
      {...{
        el: {
          label: pair.val as string,
          Svg: pair.Svg,
        },
        $justify: "start",
        testID: `card__${pair.key}`,
      }}
    />
  ));
};

export default JobApplHeader;
