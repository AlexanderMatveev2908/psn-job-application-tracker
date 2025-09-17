import test from "@playwright/test";
import { getByID } from "../../../lib_tests/shortcuts/get";
import { clickByID } from "../../../lib_tests/shortcuts/click";
import { checkIsFocused } from "../../../lib_tests/shortcuts/check";
import { preAuthRegister } from "../pre";
import { waitTmr } from "../../../lib_tests/shortcuts/wait";

test("register form swap err mgmt", async ({ browser }) => {
  const { form, page } = await preAuthRegister(browser);

  const firstName = await getByID(form, "first_name");

  await firstName.fill("validFirstName");

  await clickByID(form, "btns_swapper_next_swap");

  await waitTmr(page);

  const pwd = await getByID(form, "password");
  await checkIsFocused(pwd);

  await clickByID(form, "register__form__submit");

  await waitTmr(page);
  const lastName = await getByID(form, "last_name");
  await checkIsFocused(lastName);

  await clickByID(form, "btns_swapper_next_swap");

  await waitTmr(page);

  await checkIsFocused(pwd);

  await clickByID(form, "btns_swapper_prev_swap");

  await waitTmr(page);

  await checkIsFocused(firstName);
});
