import test from "@playwright/test";
import { getAccessManageAcc } from "../../lib_tests/actions/user";
import { clickByID, getByID, getByTxt } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";

test("setup 2FA user not verified", async ({ browser }) => {
  const { container, page } = await getAccessManageAcc(browser);

  for (let i = 0; i < 2; i++) {
    await clickByID(container, "btns_swapper_next_swap");
    await waitTmr(page);
  }

  const form = await getByID(container, "setup_2FA__swap");

  await getByTxt(form, "User need to confirm account before setup 2FA");
});
