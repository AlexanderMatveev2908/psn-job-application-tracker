import test from "@playwright/test";
import { clickByID, getByID, isToastOk } from "../../../lib_tests/idx";
import { waitTmr, waitURL } from "../../../lib_tests/shortcuts/wait";
import { getAccessManageAcc } from "../../../lib_tests/actions/user";

test("del acc ok", async ({ browser }) => {
  const { page, container } = await getAccessManageAcc(browser);

  for (let i = 0; i < 3; i++) {
    await clickByID(container, "btns_swapper_next_swap");
    await waitTmr(page);
  }

  const form = await getByID(container, "delete_account__swap");

  await clickByID(form, "delete_account__btn");

  await clickByID(page, "pop__delete__btn");

  await waitURL(page, "/notice");

  await isToastOk(page, "account deleted");
});
