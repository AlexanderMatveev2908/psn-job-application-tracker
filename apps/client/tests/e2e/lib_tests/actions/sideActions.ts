import { expect, Page } from "@playwright/test";
import { clickByID } from "../shortcuts/click";
import { getByID, getByTxt } from "../shortcuts/get";
import { hexToRgb } from "@/core/lib/dataStructure/parsers";

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
