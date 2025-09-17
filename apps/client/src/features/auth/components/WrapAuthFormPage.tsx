/** @jsxImportSource @emotion/react */
"use client";

import WrapFormPage, {
  WrapFormPagePropsType,
} from "../../../common/components/forms/wrappers/WrapFormPage/WrapFormPage";
import { FieldValues } from "react-hook-form";
import AuthSpannerLinks from "@/features/auth/components/AuthSpannerLinks/AuthSpannerLinks";

type PropsType<T extends FieldValues> = Omit<
  WrapFormPagePropsType<T>,
  "AdditionalFooterNode"
>;

const WrapAuthFormPage = <T extends FieldValues>({
  propsProgressSwap,
  children,
  formCtx,
  handleSave,
  formTestID,
  isLoading,
  propsBtnsSwapper,
}: PropsType<T>) => {
  return (
    <WrapFormPage
      {...{
        formCtx,
        handleSave,
        formTestID,
        isLoading,
        propsBtnsSwapper,
        propsProgressSwap,
        AdditionalFooterNode: () => <AuthSpannerLinks />,
      }}
    >
      {children}
    </WrapFormPage>
  );
};

export default WrapAuthFormPage;
