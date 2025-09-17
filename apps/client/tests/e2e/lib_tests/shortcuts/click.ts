import { Locator, Page } from "@playwright/test";
import { getByID, getByTxt } from "./get";

export const clickByID = async (loc: Page | Locator, id: string) => {
  const el = await getByID(loc, id);
  await el.click();
};

export const clickByTxt = async (loc: Locator | Page, txt: string) => {
  const el = await getByTxt(loc, txt);

  await el.click();
};
