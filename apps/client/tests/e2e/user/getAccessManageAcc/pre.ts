import { Browser } from "@playwright/test";
import { getByID, getTokensLib } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

export const preUserAccessAcc = async (browser: Browser) => {
  const { page, payload } = await getTokensLib(browser, {});

  await page.goto("/user/manage-account");

  await waitURL(page, "/user/access-manage-account");

  return {
    page,
    form: await getByID(page, "manage_acc__form"),
    payload,
  };
};
