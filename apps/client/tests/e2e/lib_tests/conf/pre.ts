import { Browser } from "@playwright/test";
import { goPage } from "../shortcuts/go";

export const preTest = async (browser: Browser, url: string) => {
  const page = await (await browser.newContext()).newPage();

  await goPage(page, url);

  return page;
};
