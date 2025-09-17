import test from "@playwright/test";
import { goFormPreManageAcc2FA } from "../../lib_tests/actions/user";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";

test("manage acc 2FA TOTP ok", async ({ browser }) => {
  const { totp_secret, page } = await goFormPreManageAcc2FA(browser);

  await submitFormTOTP(page, { totp_secret, url: "/user/manage-account" });
});

test("manage acc 2FA backup code ok", async ({ browser }) => {
  const { page, backup_codes } = await goFormPreManageAcc2FA(browser);

  await submitFormBackupCode(page, {
    backup_codes,
    url: "/user/manage-account",
  });
});
