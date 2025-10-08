import { TokenT } from "@/common/types/tokens";
import { Browser } from "@playwright/test";
import { getByID, getUser2FA, preTest } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

export const preRecoverPwd2FA = async (browser: Browser) => {
  const { cbcHmacToken, ...rst } = await getUser2FA(browser, {
    tokenType: TokenT.RECOVER_PWD,
  });

  const page = await preTest(browser, `/verify?cbcHmacToken=${cbcHmacToken}`);

  await waitURL(page, "/verify/recover-password-2FA");

  const form2FA = await getByID(page, "2FA__form");

  return {
    ...rst,
    cbcHmacToken,
    form2FA,
    page,
  };
};
