import test from "@playwright/test";
import { getTokensLib, isToastOk, preTest } from "../../lib_tests/idx";
import { TokenT } from "@/common/types/tokens";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { changeEmailOk } from "../../lib_tests/actions/user";

test("conf new email", async ({ browser }) => {
  const { payload } = await changeEmailOk(browser);

  const { cbc_hmac_token } = await getTokensLib(browser, {
    tokenType: TokenT.CHANGE_EMAIL,
    payload,
  });

  const page = await preTest(browser, "/");

  await page.goto(`/verify?cbc_hmac_token=${cbc_hmac_token}`);

  await waitURL(page, "/");

  await isToastOk(page, "email updated successfully");
});
