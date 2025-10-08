import { StoreStateT } from "@/core/store";
import {
  createEntityAdapter,
  createSlice,
  PayloadAction,
} from "@reduxjs/toolkit";
import { JobApplicationT } from "../types";

const jobApplicationsAdapter = createEntityAdapter<JobApplicationT>();

const initState = jobApplicationsAdapter.getInitialState({
  nHits: 0,
  pages: 0,
});

export type PayloadSetState = {
  nHits: number;
  pages: number;
  jobApplications: JobApplicationT[];
};

export const jobApplicationsSlice = createSlice({
  name: "jobApplications",
  initialState: initState,
  reducers: {
    addJob: jobApplicationsAdapter.addOne,
    delJob: jobApplicationsAdapter.removeOne,
    patchJob: jobApplicationsAdapter.updateOne,
    setJobs: jobApplicationsAdapter.setAll,

    setData: (state, action: PayloadAction<PayloadSetState>) => {
      const { jobApplications, nHits, pages } = action.payload;

      state.nHits = nHits;
      state.pages = pages;

      jobApplicationsAdapter.setAll(state, jobApplications);
    },

    insertJobAt: (
      state,
      action: PayloadAction<{ idx: number; appl: JobApplicationT }>
    ) => {
      const { idx, appl } = action.payload;

      const all = jobApplicationsAdapter.getSelectors().selectAll(state);

      const updated = [...all.slice(0, idx), appl, ...all.slice(idx)];

      jobApplicationsAdapter.setAll(state, updated);
    },
  },
});

export const getJobApplicationsState = (state: StoreStateT) =>
  state.jobApplications;

export const jobsSelectors = jobApplicationsAdapter.getSelectors(
  (state: StoreStateT) => state.jobApplications
);

export const getJobByID = jobsSelectors.selectById;

export const getJobList = jobsSelectors.selectAll;
