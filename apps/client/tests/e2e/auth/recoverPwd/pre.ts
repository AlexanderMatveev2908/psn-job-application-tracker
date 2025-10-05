import { TokenT } from "@/common/types/tokens";
import { Browser } from "@playwright/test";
import { getByID, getTokensLib } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

export const preAuthRecoverPwd = async (browser: Browser) => {
  const { cbcHmacToken, plainPwd } = await getTokensLib(browser, {
    tokenType: TokenT.RECOVER_PWD,
  });

  const page = await (await browser.newContext()).newPage();

  await page.goto(`/verify?cbcHmacToken=${cbcHmacToken}`);

  await waitURL(page, "/auth/recover-password");

  const form = await getByID(page, "recover_pwd__form");

  return {
    form,
    page,
    plainPwd,
  };
};
