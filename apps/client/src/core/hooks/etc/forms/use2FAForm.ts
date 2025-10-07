import { useForm } from "react-hook-form";
import { useListenHeight } from "../height/useListenHeight";
import { useSwap } from "../useSwap/useSwap";
import {
  BackupCodeFormT,
  resetValsBackupForm,
  resetValsTotpForm,
  schemaBackupForm,
  schemaTotpCode,
  ToptFormT,
} from "@/core/paperwork";
import { zodResolver } from "@hookform/resolvers/zod";
import { useUser } from "@/features/user/hooks/useUser";
import { useCallback } from "react";
import { useKitHooks } from "../useKitHooks";
import { TriggerApiT, UnwrappedResApiT } from "@/common/types/api";
import { useCheckTypeCbcHmac } from "../tokens/useCheckTypeCbcHmac";
import { TokenT } from "@/common/types/tokens";
import { logFormErrs } from "@/core/lib/forms";

type Params<T> = {
  mutationTrigger: TriggerApiT<T>;
  successCb: (res: UnwrappedResApiT<T>) => void;
  delCbcOnSuccess: boolean;
  tokenType: TokenT;
};

export const use2FAForm = <T>({
  mutationTrigger,
  successCb,
  delCbcOnSuccess,
  tokenType,
}: Params<T>) => {
  useCheckTypeCbcHmac({ tokenType });

  const { startSwap, swapState } = useSwap();
  const { currSwap } = swapState;

  const { userState, delCbcHmac } = useUser();
  const { wrapAPI } = useKitHooks();

  const formTotpCtx = useForm<ToptFormT>({
    mode: "onChange",
    resolver: zodResolver(schemaTotpCode),
    defaultValues: resetValsTotpForm,
  });
  const { handleSubmit: submitTotp, reset: resetTotp } = formTotpCtx;

  const formBackupCodeCtx = useForm<BackupCodeFormT>({
    mode: "onChange",
    resolver: zodResolver(schemaBackupForm),
    defaultValues: resetValsBackupForm,
  });
  const { handleSubmit: submitBackupCode, reset: resetBackup } =
    formBackupCodeCtx;

  const mainCb = useCallback(
    async ({
      totpCode,
      backupCode,
    }: {
      totpCode?: string;
      backupCode?: string;
    }) => {
      const res = await wrapAPI({
        cbAPI: () =>
          mutationTrigger({
            cbcHmacToken: userState.cbcHmacToken,
            ...(totpCode ? { totpCode } : { backupCode }),
          }),
        pushNotice: [401],
      });

      if (!res) return;

      if (delCbcOnSuccess) delCbcHmac();

      resetTotp(resetValsTotpForm);
      resetBackup(resetValsBackupForm);

      successCb(res);
    },
    [
      userState.cbcHmacToken,
      wrapAPI,
      delCbcHmac,
      mutationTrigger,
      successCb,
      resetBackup,
      resetTotp,
      delCbcOnSuccess,
    ]
  );

  const { contentRef, contentH } = useListenHeight({
    opdDep: [currSwap],
  });

  const handleSaveBackupCode = submitBackupCode(async (data) => {
    await mainCb({
      backupCode: data.backupCode,
    });
  }, logFormErrs);

  const handleSaveTotp = submitTotp(async (data) => {
    await mainCb({
      totpCode: data.totpCode.join(""),
    });
  }, logFormErrs);

  return {
    startSwap,
    swapState,
    contentRef,
    contentH,
    totpProps: {
      formCtx: formTotpCtx,
      handleSave: handleSaveTotp,
    },
    backupCodeProps: {
      formCtx: formBackupCodeCtx,
      handleSave: handleSaveBackupCode,
    },
  };
};
