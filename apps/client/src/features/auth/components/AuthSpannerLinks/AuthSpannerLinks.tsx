/** @jsxImportSource @emotion/react */
"use client";

import { usePathname } from "next/navigation";
import type { FC } from "react";
import { authSpannerLinks, AuthSpannerLinksT } from "./uiFactory/idx";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import { LinkAppSvgT } from "@/common/types/ui";
import LinkSvg from "@/common/components/links/LinkSvg";
import { extractNamePagePath } from "@/core/lib/path";

const AuthSpannerLinks: FC = () => {
  const p = usePathname();
  const isAuthLayout =
    ["register", "login"].some((allowed) => p.includes(allowed)) ||
    p.includes("require-email");

  const links =
    authSpannerLinks?.[
      extractNamePagePath(p, { usFriendly: false }) as AuthSpannerLinksT
    ];
  const { ids } = useGenIDs({ lengths: [links?.length] });

  return !isAuthLayout ? null : (
    <div className="w-full grid grid-cols-2 items-center justify-items-center py-[25px]">
      {links.map((el, i) => {
        const Svg = (el.link as LinkAppSvgT).Svg;

        return (
          <LinkSvg
            key={ids[0]?.[i]}
            {...{
              href: (el.link as LinkAppSvgT).href,
              Svg,
              confPortal: {
                showPortal: true,
              },
              tooltipTxt: el.msg as string,
            }}
          />
        );
      })}
    </div>
  );
};

export default AuthSpannerLinks;
