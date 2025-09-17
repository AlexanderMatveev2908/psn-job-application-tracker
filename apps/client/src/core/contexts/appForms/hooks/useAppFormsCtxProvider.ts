import {
  resetValsSearchJobs,
  SearchJobsFormT,
  searchJobsSchema,
} from "@/features/jobApplications/pages/SearchJobs/paperwork";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";

export const useAppFormsCtxProvider = () => {
  const formCtxJobs = useForm<SearchJobsFormT>({
    mode: "onChange",
    resolver: zodResolver(searchJobsSchema),
    defaultValues: resetValsSearchJobs,
  });

  return {
    formCtxJobs,
  };
};

export type AppFormsCtxT = ReturnType<typeof useAppFormsCtxProvider>;
