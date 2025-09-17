/** @jsxImportSource @emotion/react */
"use client";

import JobApplicationForm from "@/features/jobApplications/forms/JobApplicationForm/JobApplicationForm";
import type { FC } from "react";
import { FormProvider } from "react-hook-form";
import { jobApplicationSliceAPI } from "@/features/jobApplications/slices/api";
import { useJobApplForm } from "@/features/jobApplications/hooks/useJobApplForm";

const Page: FC = () => {
  const [mutate] = jobApplicationSliceAPI.useAddJobApplicationMutation();

  const { handleSave, formCtx } = useJobApplForm({ mutate });

  return (
    <FormProvider {...formCtx}>
      <JobApplicationForm {...{ handleSave }} />
    </FormProvider>
  );
};

export default Page;
