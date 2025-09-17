import test, { expect } from "@playwright/test";
import { clickByID, getByID, isToastOk } from "../../lib_tests/idx";
import AdmZip from "adm-zip";
import { getAccessManageAccVerified } from "../../lib_tests/actions/user";

test("setup 2FA ok", async ({ browser }) => {
  const { swap, page } = await getAccessManageAccVerified(browser);

  await clickByID(swap, "setup_2FA__btn");

  await isToastOk(page);

  const qrLk = await getByID(swap, "qr_code_result");
  const zipBtn = await getByID(swap, "setup_2FA__link");

  const [imgFile] = await Promise.all([
    page.waitForEvent("download"),
    qrLk.click(),
  ]);

  await expect(imgFile.suggestedFilename()).toMatch(/^qrcode.*\.png$/);

  const [zipFile] = await Promise.all([
    page.waitForEvent("download"),
    zipBtn.click(),
  ]);

  await expect(zipFile.suggestedFilename()).toMatch(/^2FA.*\.zip$/);

  await clickByID(swap, "cpy_totp__btn");

  const totpCopied = await page.evaluate(() => navigator.clipboard.readText());

  const zipObj = new AdmZip(await zipFile.path());
  const zipEntries = zipObj.getEntries();
  const totpFile = zipEntries.find((el) => el.entryName === "totp_secret.txt");

  if (!totpFile) throw new Error("TOTP not found in zip");

  const totpZip = totpFile.getData().toString("utf-8").trim();

  await expect(totpZip).toBe(totpCopied);

  await clickByID(swap, "cpy_backup_codes__btn");

  const backupCodes = await page.evaluate(() => navigator.clipboard.readText());

  const backupFile = zipEntries.find(
    (el) => el.entryName === "backup_codes.txt"
  );
  if (!backupFile) throw new Error("backup codes not found in zip");

  const zipCodes = backupFile.getData().toString("utf-8").trim();

  const REG_BACKUP_CODE = /[A-F0-9]{4}-[A-F0-9]{4}/g;

  const parsedCopiedCodes = backupCodes.match(REG_BACKUP_CODE) ?? [];
  const parsedZipCodes = zipCodes.match(REG_BACKUP_CODE) ?? [];

  expect(new Set(parsedCopiedCodes)).toEqual(new Set(parsedZipCodes));
});
