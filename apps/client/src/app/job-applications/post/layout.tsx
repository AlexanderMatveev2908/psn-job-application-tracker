import BaseLayoutPage from "@/common/components/wrappers/pages/BaseLayoutPage";
import { ChildrenT } from "@/common/types/ui";
import type { FC } from "react";

const Layout: FC<ChildrenT> = ({ children }) => {
  return (
    <BaseLayoutPage
      {...{
        title: "Add Job Application",
      }}
    >
      {children}
    </BaseLayoutPage>
  );
};

export default Layout;
