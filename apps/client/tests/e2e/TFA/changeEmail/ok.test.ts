import test from "@playwright/test";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { preChangeEmail2FA } from "./pre";

test("change email totp ok", async ({ browser }) => {
  const { page, totp_secret } = await preChangeEmail2FA(browser);

  await waitURL(page, "/verify/change-email-2FA");

  await submitFormTOTP(page, { totp_secret, url: "/" });
});

test("change email backup code ok", async ({ browser }) => {
  const { page, backup_codes } = await preChangeEmail2FA(browser);

  await waitURL(page, "/verify/change-email-2FA");

  await submitFormBackupCode(page, { backup_codes, url: "/" });
});
