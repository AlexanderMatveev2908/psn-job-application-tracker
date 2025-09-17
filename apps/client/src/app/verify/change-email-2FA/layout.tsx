import BaseLayoutPage from "@/common/components/wrappers/pages/BaseLayoutPage";
import { ChildrenT } from "@/common/types/ui";
import type { FC } from "react";

const Layout: FC<ChildrenT> = ({ children }) => {
  return (
    <BaseLayoutPage
      {...{
        title: "Change Email 2FA",
      }}
    >
      {children}
    </BaseLayoutPage>
  );
};

export default Layout;
