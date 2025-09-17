/** @jsxImportSource @emotion/react */
"use client";

import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { JobApplicationT } from "@/features/jobApplications/types";
import type { FC } from "react";
import { btnsFooter } from "../../uiFactory/cards";
import LinkShadow from "@/common/components/links/LinkShadow";
import BtnShadow from "@/common/components/buttons/BtnShadow";

type PropsType = {
  job: JobApplicationT;
  handleOpenPop: (el: JobApplicationT) => void;
};

const JobApplFooter: FC<PropsType> = ({ job, handleOpenPop }) => {
  const {
    ids: [ids],
  } = useGenIDs({ lengths: [2] });

  return (
    <div className="w-full flex justify-around items-center gap-8">
      {btnsFooter.map((el, idx) => (
        <div key={ids[idx]} className="w-[200px]">
          {!idx ? (
            <LinkShadow
              {...{
                act: "INFO",
                href: `/job-applications/put/${job.id}`,
                el,
              }}
            />
          ) : (
            <BtnShadow
              {...{
                act: "ERR",
                el,
                handleClick: () => handleOpenPop(job),
                testID: "btn__del_appl",
              }}
            />
          )}
        </div>
      ))}
    </div>
  );
};

export default JobApplFooter;
