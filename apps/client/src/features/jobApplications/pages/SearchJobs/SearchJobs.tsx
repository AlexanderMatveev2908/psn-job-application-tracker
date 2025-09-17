/* eslint-disable @typescript-eslint/no-explicit-any */
/** @jsxImportSource @emotion/react */
"use client";

import SearchBar from "@/features/layout/components/SearchBar/SearchBar";
import {
  filtersSearchJobs,
  searchJobsFieldsTxt,
  sortersSearchJobs,
} from "./uiFactory/search";
import { resetValsSearchJobs, SearchJobsFormT } from "./paperwork";
import { FormFieldTxtSearchBarT } from "@/common/types/ui";
import WrapCSR from "@/common/components/wrappers/pages/WrapCSR";
import { ZodObject } from "zod";
import { useSelector } from "react-redux";
import { getJobList } from "../../slices/slice";
import JobApplItem from "./components/JobApplItem";
import Popup from "@/common/components/wrappers/Popup/Popup";
import { useCallback, useState } from "react";
import { JobApplicationT } from "../../types";
import Txt from "@/common/components/elements/txt/Txt";
import { jobApplicationSliceAPI } from "../../slices/api";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";

type PropsType<K extends any[]> = {
  hook: K;
  schema: ZodObject;
};

const SearchJobs = <K extends any[]>({ hook, schema }: PropsType<K>) => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [_, res] = hook;
  const isPending = res?.isLoading || res?.isFetching || res?.isUninitialized;

  const jobs = useSelector(getJobList);

  const [isPop, setIsPop] = useState<boolean | null>(null);
  const [applicationTarget, setApplicationTarget] =
    useState<JobApplicationT | null>(null);

  const [mutate, { isLoading }] =
    jobApplicationSliceAPI.useDelJobApplByIDMutation();
  const { wrapAPI } = useWrapAPI();

  const handleDelete = async (applID: string) => {
    await wrapAPI({
      cbAPI: () => mutate(applID),
    });

    setIsPop(false);
  };

  const handleOpenPop = useCallback((el: JobApplicationT) => {
    setApplicationTarget(el);
    setIsPop(true);
  }, []);

  return (
    <div className="w-full grid grid-cols-1 gap-14">
      <SearchBar
        {...{
          allowedTxtFields:
            searchJobsFieldsTxt as FormFieldTxtSearchBarT<SearchJobsFormT>[],
          resetVals: resetValsSearchJobs,
          filters: filtersSearchJobs,
          sorters: sortersSearchJobs,
          hook,
          schema,
        }}
      />

      <WrapCSR
        {...{
          isLoading: isPending,
          $minH: "min-h-[60vh]",
        }}
      >
        <div className="w-full grid grid-cols-1 lg:grid-cols-2 gap-10 pb-[150px]">
          {jobs.map((el) => (
            <JobApplItem key={el.id} {...{ job: el, handleOpenPop }} />
          ))}
        </div>
      </WrapCSR>

      <Popup
        {...{
          isPop,
          setIsPop,
          testID: "popup__del_application",
          propsActions: {
            btns: [
              {},
              {
                handleClick: async () =>
                  await handleDelete(applicationTarget?.id as string),
                isLoading,
              },
            ],
          },
        }}
      >
        <div className="mt-5"></div>
        <Txt
          {...{
            txt: `Delete application for ${applicationTarget?.company_name}?`,
            $justify: "center",
            $size: "xl",
          }}
        />
      </Popup>
    </div>
  );
};

export default SearchJobs;
