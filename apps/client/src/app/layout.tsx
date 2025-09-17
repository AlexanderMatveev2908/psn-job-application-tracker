import type { Metadata } from "next";
import "../styles/globals.css";
import { Fira_Code } from "next/font/google";
import { genStoreSSR } from "@/core/store";
import Providers from "@/features/layout/shells/Providers";
import Toast from "@/features/layout/components/Toast/Toast";
import WrapWakeUp from "@/features/layout/shells/WrapWakeUp/WrapWakeUp";
import Header from "@/features/layout/components/Header/Header";
import Sidebar from "@/features/layout/components/Sidebar/Sidebar";
import CallbacksWrapper from "@/features/layout/shells/CallbacksWrapper";

const fira_code = Fira_Code({
  subsets: ["latin"],
  weight: ["300", "400", "500", "600", "700"],
  display: "swap",
});

export const metadata: Metadata = {
  title: "APPLICATION TRACKER",
  description: "A cool app 👻",
  icons: {
    icon: "/favicon.svg",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const store = genStoreSSR({});

  return (
    <html lang="en" data-scroll-behavior="smooth">
      <body
        className={`${fira_code.className} min-h-screen h-full w-full antialiased bg-neutral-950 text-neutral-200`}
      >
        <div
          id="portal-root"
          className="w-full h-full fixed overflow-x-hidden pointer-events-none z__bg_sidebar"
        ></div>

        <Providers
          {...{
            preloadedState: store.getState(),
          }}
        >
          <CallbacksWrapper>
            <Header />
            <Sidebar />
            <Toast />

            <WrapWakeUp>
              <div className="w-full h-full p-[25px] sm:pb-[100px]">
                {children}
              </div>
            </WrapWakeUp>
          </CallbacksWrapper>
        </Providers>
      </body>
    </html>
  );
}
