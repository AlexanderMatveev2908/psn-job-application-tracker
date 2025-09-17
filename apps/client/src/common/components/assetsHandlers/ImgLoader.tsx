/** @jsxImportSource @emotion/react */

"use client";

import { useState, type FC } from "react";
import AssetHandler from "./AssetHandler";
import Image from "next/image";

type PropsType = {
  src?: string;
  alt?: string;
};

const ImgLoader: FC<PropsType> = ({ src, alt }) => {
  const [loaded, setIsLoaded] = useState(false);
  const [isError, setIsError] = useState(false);

  return !src ? null : (
    <div className="relative h-full min-h-full min-w-full rounded-xl overflow-hidden">
      <AssetHandler {...{ loaded, isError }} />

      <Image
        src={src ?? ""}
        alt={alt ?? ""}
        className={`transition-all duration-500 object-cover h-full w-full ${
          loaded ? "opacity-100" : "opacity-0"
        }`}
        fill
        onLoad={() => setIsLoaded(true)}
        onError={() => {
          try {
            setIsError(true);
          } catch {}
        }}
      />
    </div>
  );
};

export default ImgLoader;
