import { Page } from "@playwright/test";
import { waitTmr } from "./wait";

export const goPage = async (page: Page, url: string) => {
  await page.goto(url);

  await waitTmr(page, 2500);
};
