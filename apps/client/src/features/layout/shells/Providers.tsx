/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import AppFormsProvider from "@/core/contexts/appForms/AppFormsProvider";
import { genStoreSSR } from "@/core/store";
import { useRef, type FC } from "react";
import { Provider } from "react-redux";

type PropsType = {
  children: React.ReactNode;
  preloadedState?: any;
};

const Providers: FC<PropsType> = ({ children, preloadedState }) => {
  const store = useRef(
    genStoreSSR({
      ...preloadedState,
    })
  ).current;

  return (
    <Provider store={store}>
      <AppFormsProvider>{children}</AppFormsProvider>
    </Provider>
  );
};

export default Providers;
