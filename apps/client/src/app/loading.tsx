import SpinPage from "@/common/components/elements/spinners/SpinPage/SpinPage";
import type { FC } from "react";

const loading: FC = () => {
  return (
    <SpinPage
      {...{
        act: "INFO",
      }}
    />
  );
};

export default loading;
