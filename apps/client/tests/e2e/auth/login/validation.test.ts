import test from "@playwright/test";
import { preAuthLogin } from "./pre";
import { getByID, getByTxt } from "../../lib_tests/shortcuts/get";
import { clickByID } from "../../lib_tests/shortcuts/click";
import { checkIsFocused } from "../../lib_tests/shortcuts/check";

test("login validation", async ({ browser }) => {
  const { form, page } = await preAuthLogin(browser);

  const email = await getByID(form, "email");

  await email.fill("<>@<>");

  await getByTxt(page, "invalid email");

  const pwd = await getByID(form, "password");

  await pwd.fill("12345");

  await getByTxt(page, "invalid password");

  await clickByID(form, "login__form__submit");

  await checkIsFocused(email);
});
