import { Browser } from "@playwright/test";
import { loginUserOk } from "../../lib_tests/actions/auth";
import { goPage } from "../../lib_tests/shortcuts/go";

export const preAuthLogout = async (browser: Browser) => {
  const { payload, page } = await loginUserOk(browser);

  await goPage(page, "/protected");

  return {
    payload,
    page,
  };
};
