/** @jsxImportSource @emotion/react */
"use client";

import DropMenuAbsolute from "@/common/components/dropMenus/DropMenuAbsolute";
import { css } from "@emotion/react";
import type { FC } from "react";
import { TbNotes } from "react-icons/tb";
import { JobApplicationT } from "@/features/jobApplications/types";
import PairTxtSvg from "@/common/components/elements/PairTxtSvg";
import { IoStatsChart } from "react-icons/io5";

type PropsType = {
  job: JobApplicationT;
  suffix: string;
};

const JobApplBody: FC<PropsType> = ({ job, suffix }) => {
  return (
    <div className="w-full flex gap-8 items-center justify-around flex-wrap">
      <div className="w-fit">
        <DropMenuAbsolute
          {...{
            el: {
              Svg: TbNotes,
              label: "Notes",
            },
            $cstmBtnCSS: css`
              padding: 5px 40px;
            `,
          }}
        >
          {() => (
            <div className="flex justify-center p-3">
              <span data-testid={"card__notes"} className="txt__md">
                {" "}
                {job.notes ?? "N/A"}
              </span>
            </div>
          )}
        </DropMenuAbsolute>
      </div>

      <div
        data-testid={"job_appl__card__status"}
        className="border-2 rounded-xl py-2 px-10"
        css={css`
          color: var(--${suffix});
        `}
      >
        <PairTxtSvg
          {...{
            el: {
              Svg: IoStatsChart,
              label: job.status,
            },
            testID: "card__status",
          }}
        />
      </div>
    </div>
  );
};

export default JobApplBody;
