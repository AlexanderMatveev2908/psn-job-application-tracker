import { Locator, Page } from "@playwright/test";
import { checkIsShw } from "./check";

export const getByID = async (
  loc: Page | Locator,
  id: string
): Promise<Locator> => {
  const el = loc.locator(`[data-testid='${id}']`);

  await checkIsShw(el);

  return el;
};

export const getByTxt = async (loc: Locator | Page, x: string) => {
  const el = loc.getByText(new RegExp(x, "i"));

  await checkIsShw(el);

  return el;
};
