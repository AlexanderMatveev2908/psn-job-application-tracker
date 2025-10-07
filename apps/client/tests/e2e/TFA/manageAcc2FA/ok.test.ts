import test from "@playwright/test";
import { goFormPreManageAcc2FA } from "../../lib_tests/actions/user";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";

test("manage acc 2FA TOTP ok", async ({ browser }) => {
  const { totpSecret, page } = await goFormPreManageAcc2FA(browser);

  await submitFormTOTP(page, { totpSecret, url: "/user/manage-account" });
});

test("manage acc 2FA backup code ok", async ({ browser }) => {
  const { page, bkpCodes } = await goFormPreManageAcc2FA(browser);

  await submitFormBackupCode(page, {
    bkpCodes,
    url: "/user/manage-account",
  });
});
