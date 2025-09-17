"use client";

import type { FC } from "react";
import LinkShadow from "@/common/components/links/LinkShadow";
import AppEventPage from "@/common/components/wrappers/pages/AppEventPage/AppEventPage";

const NotFound: FC = () => {
  return (
    <AppEventPage
      {...{
        act: "INFO",
        msg: "The treasure chest is empty. Someone got here before you... 💰",
      }}
    >
      <div className="w-[250px]">
        <LinkShadow
          {...{
            wrapper: "next_link",
            act: "INFO",
            href: "/",
            el: {
              label: "Home",
            },
          }}
        />
      </div>
    </AppEventPage>
  );
};

export default NotFound;
