/* eslint-disable @typescript-eslint/no-explicit-any */
import { zodResolver } from "@hookform/resolvers/zod";
import { useFieldArray, useForm } from "react-hook-form";
import {
  addJobApplicationSchema,
  resetValsJobApplForm,
} from "../forms/JobApplicationForm/paperwork/jobAppliication";
import { useAppFormsCtxConsumer } from "@/core/contexts/appForms/hooks/useAppFormsCtxConsumer";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import { genFormData, logFormErrs } from "@/core/lib/forms";
import { TriggerApiT } from "@/common/types/api";
import { companyNameField, positionNameField } from "../uiFactory";
import { JobApplicationT } from "../types";
import { useEffect } from "react";

type Params<T extends Record<string, any>> = {
  mutate: TriggerApiT<T>;
  application?: JobApplicationT;
};

export const useJobApplForm = <T extends Record<string, any>>({
  mutate,
  application,
}: Params<T>) => {
  const formCtx = useForm({
    mode: "onChange",

    resolver: zodResolver(addJobApplicationSchema) as any,
    defaultValues: resetValsJobApplForm,
  });
  const { handleSubmit, reset } = formCtx;

  const { formCtxJobs: formCtxRead } = useAppFormsCtxConsumer();
  const { control: controlRead, getValues: getValuesRead } = formCtxRead;
  const { append: appendReadForm } = useFieldArray({
    control: controlRead,
    name: "txtFields",
  });

  useEffect(() => {
    if (!application) return;

    reset({
      company_name: application.company_name,
      position_name: application.position_name,
      status: application.status,
      notes: application.notes ?? "",
      applied_at: new Date(application.applied_at).toISOString().split("T")[0],
    });
  }, [application, reset]);

  const { nav, wrapAPI } = useKitHooks();

  const handleSave = handleSubmit(async (data) => {
    const formData = genFormData(data);

    const res = await wrapAPI<T>({
      cbAPI: () =>
        application
          ? mutate({ data: formData, applicationID: application.id })
          : mutate(data),
    });

    if (!res) return;

    reset(resetValsJobApplForm);

    for (const f of [companyNameField, positionNameField])
      if (!(getValuesRead("txtFields") ?? []).some((el) => el.name === f.name))
        appendReadForm({ ...f, val: "" });

    formCtxRead.setValue("txtFields.0.val", res.job_application.company_name, {
      shouldValidate: true,
    });
    formCtxRead.setValue("txtFields.1.val", res.job_application.position_name, {
      shouldValidate: true,
    });

    nav.replace("/job-applications/read");
  }, logFormErrs);

  return {
    handleSave,
    formCtx,
  };
};
