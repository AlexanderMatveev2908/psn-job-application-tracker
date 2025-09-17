import { ChildrenT } from "@/common/types/ui";
import type { FC } from "react";
import Title from "../../elements/txt/Title";

type PropsType = {
  title?: string;
} & ChildrenT;

const BaseLayoutPage: FC<PropsType> = ({ title, children }) => {
  return (
    <div className="w-full h-fit min-h-screen flex flex-col gap-12 relative">
      {title && (
        <Title
          {...{
            title,
            $twdCls: "3xl",
          }}
        />
      )}

      {children}
    </div>
  );
};

export default BaseLayoutPage;
