import test, { expect } from "@playwright/test";
import { submitFormBackupCode, submitFormTOTP } from "../../lib_tests/idx";
import { REG_JWT } from "@/core/constants/regex";
import { preLogin2FA } from "./pre";

test("login 2FA TOTP ok", async ({ browser }) => {
  const { page, totpSecret } = await preLogin2FA(browser);

  // ? simpler fallback if my parser b32 => hex show bugs
  // const code = authenticator.generate(totp_secret);

  await submitFormTOTP(page, { totpSecret, url: "/" });

  await expect(
    await page.evaluate(() => sessionStorage.getItem("accessToken"))
  ).toMatch(REG_JWT);
});

test("login 2FA backup code ok", async ({ browser }) => {
  const { page, bkpCodes } = await preLogin2FA(browser);

  await submitFormBackupCode(page, { bkpCodes, url: "/" });

  await expect(
    await page.evaluate(() => sessionStorage.getItem("accessToken"))
  ).toMatch(REG_JWT);
});
