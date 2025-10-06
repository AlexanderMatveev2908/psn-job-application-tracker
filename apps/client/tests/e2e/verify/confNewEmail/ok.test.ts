import test from "@playwright/test";
import { getTokensLib, isToastOk, preTest } from "../../lib_tests/idx";
import { TokenT } from "@/common/types/tokens";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { changeEmailOk } from "../../lib_tests/actions/user";

test("conf new email", async ({ browser }) => {
  const { user } = await changeEmailOk(browser);

  const { cbcHmacToken } = await getTokensLib(browser, {
    tokenType: TokenT.CHANGE_EMAIL,
    payload: user,
  });

  const page = await preTest(browser, "/");

  await page.goto(`/verify?cbcHmacToken=${cbcHmacToken}`);

  await waitURL(page, "/");

  await isToastOk(page, "email changed");
});
