/** @jsxImportSource @emotion/react */
"use client";

import BtnShadow from "@/common/components/buttons/BtnShadow";
import LinkShadow from "@/common/components/links/LinkShadow";
import WrapCSR from "@/common/components/wrappers/pages/WrapCSR";
import { useWrapAPI } from "@/core/hooks/api/useWrapAPI";
import { testSliceAPI } from "@/features/test/slices/api";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { type FC } from "react";

const Home: FC = () => {
  const [mutate, { isLoading }] = testSliceAPI.usePostHelloMutation();
  const { wrapAPI } = useWrapAPI();

  const handleClick = async () => {
    await wrapAPI({
      cbAPI: () => mutate({ msg: "Client message" }),
    });
  };

  const { isUsOk } = useGetUserState();

  return (
    <WrapCSR>
      <div className="w-full h-full min-h-screen flex flex-col justify-center items-center gap-20">
        <span className="text-3xl font-bold">Script worked ✌🏽</span>

        <div className="w-[250px]">
          <BtnShadow
            {...{
              act: "OK",
              handleClick,
              el: {
                label: "Click me",
              },
              isLoading,
            }}
          />
        </div>

        <div className="w-[275px]">
          <LinkShadow
            {...{
              act: isUsOk ? "OK" : "ERR",
              handleClick,
              el: {
                label: "Protected Page",
              },
              href: "/protected",
            }}
          />
        </div>
      </div>
    </WrapCSR>
  );
};

export default Home;
