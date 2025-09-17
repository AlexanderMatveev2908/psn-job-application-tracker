import { testSlice } from "@/features/test/slices/slice";
import { combineReducers, configureStore, Middleware } from "@reduxjs/toolkit";
import { apiSlice } from "./api";
import { toastSlice } from "@/features/layout/components/Toast/slices";
import { sideSlice } from "@/features/layout/components/Sidebar/slice";
import { noticeSlice } from "@/features/notice/slices/slice";
import { userSlice } from "@/features/user/slices/slice";
import { handleErrorsActions } from "./conf/middleware/errors";
import { jobApplicationsSlice } from "@/features/jobApplications/slices/slice";

const rootReducer = combineReducers({
  test: testSlice.reducer,
  apiApp: apiSlice.reducer,
  toast: toastSlice.reducer,
  side: sideSlice.reducer,
  notice: noticeSlice.reducer,
  user: userSlice.reducer,
  jobApplications: jobApplicationsSlice.reducer,
});

export const genStoreSSR = (
  preloadedState: Partial<ReturnType<typeof rootReducer>>
) =>
  configureStore({
    reducer: rootReducer,

    middleware: (getDefMdw) =>
      getDefMdw().concat(
        apiSlice.middleware,
        handleErrorsActions as Middleware
      ),
    preloadedState,
  });

type StoreT = ReturnType<typeof genStoreSSR>;
export type StoreStateT = ReturnType<StoreT["getState"]>;
export type DispatchT = StoreT["dispatch"];
