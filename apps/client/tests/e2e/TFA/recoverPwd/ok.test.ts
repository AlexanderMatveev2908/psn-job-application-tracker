import test from "@playwright/test";
import { preRecoverPwd2FA } from "./pre";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";
import { recoverPwdOk } from "../../lib_tests/actions/auth";

test("recover pwd totp ok", async ({ browser }) => {
  const { page, totp_secret } = await preRecoverPwd2FA(browser);

  await submitFormTOTP(page, {
    totp_secret,
    url: "/auth/recover-password-2FA",
  });

  await recoverPwdOk(page);
});

test("recover pwd backup code ok", async ({ browser }) => {
  const { page, backup_codes } = await preRecoverPwd2FA(browser);

  await submitFormBackupCode(page, {
    backup_codes,
    url: "/auth/recover-password-2FA",
  });

  await recoverPwdOk(page);
});
