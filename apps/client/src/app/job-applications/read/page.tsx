/** @jsxImportSource @emotion/react */
"use client";

import { useAppFormsCtxConsumer } from "@/core/contexts/appForms/hooks/useAppFormsCtxConsumer";
import SearchJobs from "@/features/jobApplications/pages/SearchJobs/SearchJobs";
import {
  SearchJobsFormT,
  searchJobsSchema,
} from "@/features/jobApplications/pages/SearchJobs/paperwork";
import { jobApplicationSliceAPI } from "@/features/jobApplications/slices/api";
import SearchBarWrapper from "@/features/layout/components/SearchBar/sideComponents/SearchBarWrapper";
import type { FC } from "react";

const Page: FC = () => {
  const { formCtxJobs: formCtx } = useAppFormsCtxConsumer();

  const hook = jobApplicationSliceAPI.useLazyReadJobApplicationsQuery();

  return (
    <div className="page__shape">
      <SearchBarWrapper<SearchJobsFormT, typeof hook>
        {...{ formCtx, hook, schema: searchJobsSchema }}
      >
        {(arg) => <SearchJobs {...arg} />}
      </SearchBarWrapper>
    </div>
  );
};

export default Page;
