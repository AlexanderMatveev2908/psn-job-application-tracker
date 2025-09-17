import { ResApiT, TagAPI } from "@/common/types/api";
import { apiSlice } from "@/core/store/api";
import { JobApplicationT } from "../types";
import { getJobList, jobApplicationsSlice } from "./slice";
import { StoreStateT } from "@/core/store";

const BASE = "/job-applications";

export const jobApplicationSliceAPI = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    addJobApplication: builder.mutation<
      ResApiT<{ job_application: JobApplicationT }>,
      FormData
    >({
      query: (data) => ({
        url: `${BASE}/`,
        method: "POST",
        data,
      }),

      invalidatesTags: [{ type: TagAPI.JOB_APPLICATIONS, id: "LIST" }],
    }),

    readJobApplications: builder.query<
      ResApiT<{
        job_applications: JobApplicationT[];
        n_hits: number;
        pages: number;
      }>,
      string
    >({
      query: (data) => ({
        url: `${BASE}/?${data}`,
        method: "GET",
      }),

      providesTags: (res) => {
        return [
          ...(res?.job_applications?.length
            ? res.job_applications.map((el) => ({
                type: TagAPI.JOB_APPLICATIONS,
                id: el.id,
              }))
            : []),

          { type: TagAPI.JOB_APPLICATIONS, id: "LIST" },
        ];
      },

      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        try {
          const {
            data: { n_hits, job_applications, pages },
          } = await queryFulfilled;

          dispatch(
            jobApplicationsSlice.actions.setData({
              n_hits,
              pages,
              job_applications,
            })
          );
        } catch {}
      },
    }),

    putJobApplication: builder.mutation<
      ResApiT<{ job_application: JobApplicationT }>,
      { applicationID: string; data: FormData }
    >({
      query: (data) => ({
        url: `${BASE}/${data.applicationID}`,
        method: "PUT",
        data: data.data,
      }),

      invalidatesTags: (res, err, arg) => [
        {
          type: TagAPI.JOB_APPLICATIONS,
          id: res?.job_application.id ?? arg.applicationID,
        },
      ],
    }),

    getJobApplicationByID: builder.query<
      ResApiT<{ application: JobApplicationT }>,
      string
    >({
      query: (applID) => ({
        url: `${BASE}/${applID}`,
        method: "GET",
      }),
    }),

    delJobApplByID: builder.mutation<ResApiT<void>, string>({
      query: (applID) => ({
        url: `${BASE}/${applID}`,
        method: "DELETE",
      }),

      async onQueryStarted(applID, { dispatch, getState, queryFulfilled }) {
        const store = getState() as StoreStateT;

        const jobs = getJobList(store);
        const idx = jobs.findIndex((el) => el.id === applID);
        const target = jobs[idx]; // save the entity
        if (!target) return;

        dispatch(jobApplicationsSlice.actions.delJob(target.id));

        try {
          await queryFulfilled;

          dispatch(
            jobApplicationSliceAPI.util.invalidateTags([
              {
                type: TagAPI.JOB_APPLICATIONS,
                id: "LIST",
              },
            ])
          );
        } catch {
          dispatch(
            jobApplicationsSlice.actions.insertJobAt({ idx, appl: target })
          );
        }
      },
    }),
  }),
});
