/** @jsxImportSource @emotion/react */
"use client";

import { REG_ID } from "@/core/constants/regex";
import { useWrapQuery } from "@/core/hooks/api/useWrapQuery";
import JobApplicationForm from "@/features/jobApplications/forms/JobApplicationForm/JobApplicationForm";
import { useJobApplForm } from "@/features/jobApplications/hooks/useJobApplForm";
import { jobApplicationSliceAPI } from "@/features/jobApplications/slices/api";
import { useParams, useRouter } from "next/navigation";
import { type FC } from "react";
import { FormProvider } from "react-hook-form";

const Page: FC = () => {
  const nav = useRouter();

  const applicationID = useParams()?.applicationID;
  const isValid = REG_ID.test((applicationID as string) ?? "");
  if (!isValid) nav.replace("/not _found");

  const res = jobApplicationSliceAPI.useGetJobApplicationByIDQuery(
    applicationID as string,
    {
      skip: !isValid,
      refetchOnMountOrArgChange: true,
    }
  );
  const { data: { application } = {}, isLoading } = res ?? {};
  useWrapQuery({ ...res, pushNotice: "*" });

  const [mutate] = jobApplicationSliceAPI.usePutJobApplicationMutation();

  const { handleSave, formCtx } = useJobApplForm({ mutate, application });

  return (
    <FormProvider {...formCtx}>
      <JobApplicationForm
        {...{
          handleSave,
          propsCSR: {
            isLoading,
          },
        }}
      />
    </FormProvider>
  );
};

export default Page;
