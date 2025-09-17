import { Browser } from "@playwright/test";
import { getByID, preTest } from "../../lib_tests/idx";

export const preAuthLogin = async (browser: Browser) => {
  const page = await preTest(browser, "auth/login");

  return { page, form: await getByID(page, "login__form") };
};
