import test from "@playwright/test";
import { extractInitialsUser } from "@/core/lib/dataStructure/formatters";
import { preAuthLogout } from "./pre";
import { clickByID, getByID, getByTxt } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

test("logout header ok", async ({ browser }) => {
  const { payload, page } = await preAuthLogout(browser);

  const drop = await getByID(page, "header__toggle_drop");

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  await getByTxt(drop, extractInitialsUser(payload as any).toLowerCase());

  await drop.click();

  await clickByID(page, "header_link__logout");

  await waitURL(page, "/");

  await getByTxt(page, "logout successful");
});
