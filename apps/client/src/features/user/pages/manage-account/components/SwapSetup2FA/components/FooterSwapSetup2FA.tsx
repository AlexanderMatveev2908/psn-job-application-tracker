/** @jsxImportSource @emotion/react */
"use client";

import BtnShadow from "@/common/components/buttons/BtnShadow";
import LinkShadow from "@/common/components/links/LinkShadow";
import { Setup2FAReturnT } from "@/features/user/slices/api";
import { UserT } from "@/features/user/types";
import type { FC } from "react";

type PropsType = {
  res2FA: Setup2FAReturnT | null;
  testID: string;
  isLoading: boolean;
  handleClick: () => void;
  user: UserT | null;
};

const FooterSwapSetup2FA: FC<PropsType> = ({
  res2FA,
  testID,
  isLoading,
  handleClick,
  user,
}) => {
  return (
    <div className="mt-[50px] w-[250px] justify-self-center">
      {res2FA ? (
        <LinkShadow
          {...{
            el: {
              label: "Download Zip",
            },
            testID: `${testID}__link`,
            href: res2FA.zip_file,
            act: "INFO",
            download: "2FA.zip",
          }}
        />
      ) : (
        !user?.use_2FA && (
          <BtnShadow
            {...{
              el: {
                label: "Submit",
              },
              testID: `${testID}__btn`,
              isLoading,
              act: "INFO",
              handleClick,
            }}
          />
        )
      )}
    </div>
  );
};

export default FooterSwapSetup2FA;
