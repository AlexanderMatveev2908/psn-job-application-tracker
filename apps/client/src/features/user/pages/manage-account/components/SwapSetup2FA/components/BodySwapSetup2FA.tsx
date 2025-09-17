/** @jsxImportSource @emotion/react */
"use client";

import { Setup2FAReturnT } from "@/features/user/slices/api";
import { useMemo, type FC } from "react";
import { SwapModeT } from "@/core/hooks/etc/useSwap/etc/initState";
import ImgLoader from "@/common/components/assetsHandlers/ImgLoader";
import CpyPaste from "@/common/components/HOC/CpyPaste/CpyPaste";

type PropsType = {
  res2FA: Setup2FAReturnT;
  isCurr: boolean;
  swapMode: SwapModeT;
};

const BodySwapSetup2FA: FC<PropsType> = ({ res2FA, isCurr, swapMode }) => {
  const portalConf = useMemo(
    () => ({
      showPortal: isCurr && swapMode !== "swapping",
      optDep: [isCurr, swapMode],
    }),
    [isCurr, swapMode]
  );

  return (
    <div className="w-full grid grid-cols-1 md:grid-cols-2 justify-items-center items-center">
      <a
        data-testid={"qr_code_result"}
        download={"qrcode.png"}
        href={res2FA.totp_secret_qrcode}
        className="w-[250px] h-[250px] my-[30px]"
      >
        <ImgLoader {...{ src: res2FA.totp_secret_qrcode }} />
      </a>

      <div className="cont__grid__lg justify-items-center h-fit items-center">
        <div className="w-[250px]">
          <CpyPaste
            {...{
              testID: "cpy_totp__btn",
              portalConf,
              txt: res2FA.totp_secret,
              label: "TOTP Secret",
            }}
          />
        </div>
        <div className="w-[250px]">
          <CpyPaste
            {...{
              portalConf,
              testID: "cpy_backup_codes__btn",
              txt: (() => {
                let txt = "";

                for (let i = 0; i < 8; i += 2) {
                  txt += res2FA.backup_codes.slice(i, i + 2).join("  ") + "\n";
                }

                return txt;
              })(),
              label: "Backup Codes",
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default BodySwapSetup2FA;
