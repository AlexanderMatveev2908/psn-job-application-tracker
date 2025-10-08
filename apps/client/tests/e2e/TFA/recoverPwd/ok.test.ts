import test from "@playwright/test";
import { preRecoverPwd2FA } from "./pre";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";
import { recoverPwdOk } from "../../lib_tests/actions/auth";

test("recover pwd totp ok", async ({ browser }) => {
  const { page, totpSecret } = await preRecoverPwd2FA(browser);

  await submitFormTOTP(page, {
    totpSecret,
    url: "/auth/recover-password-2FA",
  });

  await recoverPwdOk(page);
});

test("recover pwd backup code ok", async ({ browser }) => {
  const { page, bkpCodes } = await preRecoverPwd2FA(browser);

  await submitFormBackupCode(page, {
    bkpCodes,
    url: "/auth/recover-password-2FA",
  });

  await recoverPwdOk(page);
});
