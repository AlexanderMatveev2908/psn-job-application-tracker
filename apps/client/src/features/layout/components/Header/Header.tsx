/** @jsxImportSource @emotion/react */
"use client";

import SvgBurger from "@/common/components/SVGs/Burger";
import SvgLogo from "@/common/components/SVGs/Logo";
import { css } from "@emotion/react";
import Link from "next/link";
import { useRef, type FC } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getSideState, sideSlice } from "../Sidebar/slice";
import SvgClose from "@/common/components/SVGs/Close";
import { TbUserFilled } from "react-icons/tb";
import DropMenuAbsolute from "@/common/components/dropMenus/DropMenuAbsolute";
import { linksAll, linksNonLoggedAccount } from "@/core/uiFactory/links";
import { useGenIDs } from "@/core/hooks/etc/useGenIDs";
import HeaderLink from "./components/HeaderLink";
import { usePathname } from "next/navigation";
import { headerHight } from "@/core/constants/style";
import { useHydration } from "@/core/hooks/etc/hydration/useHydration";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { extractInitialsUser } from "@/core/lib/dataStructure/formatters";
import HeaderLogout from "./components/HeaderLogout";
import { calcIsCurrPath } from "@/core/lib/path";
import { useLinksLogged } from "@/core/hooks/etc/navigation/useLinksLogged";

const Header: FC = () => {
  const sideState = useSelector(getSideState);
  const usState = useGetUserState();
  const dropRef = useRef(null);

  const { isHydrated } = useHydration();

  const path = usePathname();
  const dispatch = useDispatch();

  const { ids } = useGenIDs({
    lengths: [linksAll.length + linksNonLoggedAccount.length],
  });

  const { linksLoggedFiltered } = useLinksLogged();
  return (
    <div
      className="z__header w-full sticky top-0 left-0 border-b-3 bg-neutral-950 border-w__0 flex items-center justify-between px-3"
      css={css`
        height: ${headerHight}px;
      `}
    >
      <Link href={"/"}>
        <SvgLogo className="svg__xl" />
      </Link>

      <div ref={dropRef} className="w-fit flex items-center gap-10">
        <DropMenuAbsolute
          {...{
            isEnabled: isHydrated,
            testID: "header__toggle_drop",
            el: {
              Svg: !usState.isUsOk ? TbUserFilled : null,
              label: usState.isUsOk
                ? extractInitialsUser(usState!.user!)
                : null,
            },
            $cstmDropCSS: css`
              left: -200px;
            `,
            $cstmBtnCSS: css`
              padding: ${usState.isUsOk ? "6px 10px" : "6px"};
            `,
          }}
        >
          {({ setIsOpen }) => (
            <>
              {[
                ...(!usState.isUsOk
                  ? linksNonLoggedAccount
                  : linksLoggedFiltered),
              ].map((lk, i) => (
                <HeaderLink
                  key={ids[0][i]}
                  {...{
                    isCurrPath: calcIsCurrPath(path, lk.href),
                    lk,
                    handleClick: () => setIsOpen(false),
                  }}
                />
              ))}

              {usState.isUsOk && (
                <HeaderLogout
                  {...{
                    handleClick: () => setIsOpen(false),
                  }}
                />
              )}
            </>
          )}
        </DropMenuAbsolute>

        <button
          disabled={!isHydrated}
          data-testid={"header__toggle_sidebar"}
          key={sideState.isOpen + ""}
          onClick={() => dispatch(sideSlice.actions.toggleSide())}
          className="btn__app"
          style={
            {
              "--scale__up": 1.3,
            } as React.CSSProperties
          }
        >
          {sideState.isOpen ? (
            <SvgClose className="svg__xl" fill="var(--red__600)" />
          ) : (
            <SvgBurger className="svg__xl text-w__0" />
          )}
        </button>
      </div>
    </div>
  );
};

export default Header;
