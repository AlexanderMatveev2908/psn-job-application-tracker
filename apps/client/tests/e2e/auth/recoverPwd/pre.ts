import { TokenT } from "@/common/types/tokens";
import { Browser } from "@playwright/test";
import { getByID, getTokensLib } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

export const preAuthRecoverPwd = async (browser: Browser) => {
  const { cbc_hmac_token, payload } = await getTokensLib(browser, {
    tokenType: TokenT.RECOVER_PWD,
  });

  const page = await (await browser.newContext()).newPage();

  await page.goto(`/verify?cbc_hmac_token=${cbc_hmac_token}`);

  await waitURL(page, "/auth/recover-password");

  const form = await getByID(page, "recover_pwd__form");

  return {
    form,
    page,
    payload,
  };
};
