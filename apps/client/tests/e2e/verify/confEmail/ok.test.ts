import test, { expect } from "@playwright/test";
import { getTokensLib } from "../../lib_tests/actions/fullActions";
import { TokenT } from "@/common/types/tokens";
import { REG_JWE, REG_JWT } from "@/core/constants/regex";
import { waitURL } from "../../lib_tests/shortcuts/wait";

test("conf email ok", async ({ browser }) => {
  const { cbc_hmac_token } = await getTokensLib(browser, {
    tokenType: TokenT.CONF_EMAIL,
  });

  const page = await (await browser.newContext()).newPage();

  await page.goto(`/verify?cbc_hmac_token=${cbc_hmac_token}`);

  await waitURL(page, "/");

  const jwt = await page.evaluate(() => sessionStorage.getItem("access_token"));
  const cookies = await page.context().cookies();
  const jwe = cookies.find((c) => c.name === "refresh_token");

  expect(REG_JWT.test(jwt ?? ""));
  expect(REG_JWE.test((jwe ?? "") as string));
});
