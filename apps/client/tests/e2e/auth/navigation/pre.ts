import { Browser } from "@playwright/test";
import { closeToast, getByTxt, preTest } from "../../lib_tests/idx";

export const preAuthNavigation = async (browser: Browser) => {
  const page = await preTest(browser, "/");

  await closeToast(page);

  await getByTxt(page, "Script worked ✌🏽");

  return {
    page,
  };
};
