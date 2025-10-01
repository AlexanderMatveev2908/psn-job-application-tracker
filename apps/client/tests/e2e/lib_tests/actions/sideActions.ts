import { expect, Page } from "@playwright/test";
import { clickByID } from "../shortcuts/click";
import { getByID, getByTxt } from "../shortcuts/get";
import { b32ToHex, hexToRgb } from "@/core/lib/dataStructure/parsers";
import { totp } from "otplib";
import type { KeyEncodings } from "@otplib/core";
import { waitURL } from "../shortcuts/wait";
import { checkIsFocused } from "../idx";

export const closeToast = async (page: Page): Promise<undefined> => {
  try {
    const toast = await getByID(page, "toast");

    if (toast) await clickByID(toast, "toast__close_btn");
  } catch {}
};

const getToast = async (page: Page) => await getByID(page, "toast");

export const isToastOk = async (page: Page, msg?: string) => {
  const toast = await getToast(page);

  await expect(toast).toHaveCSS("border-color", hexToRgb("#16a34a"));

  await getByTxt(toast, "ok");

  if (msg) await getByTxt(toast, msg);
};

export const isToastErr = async (page: Page) => {
  const toast = await getToast(page);

  await expect(toast).toHaveCSS("border-color", hexToRgb("#dc2626"));

  await getByTxt(toast, "err");
};

export const submitFormTOTP = async (
  page: Page,
  { totp_secret, url }: { totp_secret: string; url: string }
) => {
  const form = await getByID(page, "totp_code__form");

  const firstSquare = await getByID(form, "totp_code.0");
  await firstSquare.click();

  await expect(firstSquare).toBeFocused();

  totp.options = { encoding: "hex" as KeyEncodings };

  const code = totp.generate(b32ToHex(totp_secret));

  await page.evaluate((c) => navigator.clipboard.writeText(c), code);
  await page.keyboard.press("Control+V");

  await clickByID(form, "totp_code__form__submit");

  await waitURL(page, url);
  await isToastOk(page);
};

export const submitFormBackupCode = async (
  page: Page,
  { backup_codes, url }: { backup_codes: string[]; url: string }
) => {
  await clickByID(page, "btns_swapper_next_swap");

  const form = await getByID(page, "backup_code__form");
  const codeInput = await getByID(form, "backup_code");

  await checkIsFocused(codeInput);

  await codeInput.fill(backup_codes[0]);

  await clickByID(form, "backup_code__form__submit");

  await waitURL(page, url);
  await isToastOk(page);
};
