import { expect, Locator, Page } from "@playwright/test";
import { getByTxt } from "./get";

export const checkTxtList = async (page: Page, msgs: string[]) => {
  for (const x of msgs) {
    await getByTxt(page, x);
  }
};

export const checkLinksList = async (parent: Locator, arg: string[]) => {
  for (const name of arg) {
    const lk = parent.getByRole("link", { name });
    await lk.waitFor({ state: "visible", timeout: 30 * 1000 });
  }
};

export const checkIsShw = async (el: Locator) => {
  await el.first().waitFor({ state: "visible", timeout: 30 * 1000 });
};

export const checkIsFocused = async (el: Locator) => {
  await expect(el).toBeFocused({ timeout: 30 * 1000 });
};
