import test from "@playwright/test";
import { preRequireEmail } from "./pre";
import { getByID, getByTxt } from "../../lib_tests/shortcuts/get";
import { clickByID } from "../../lib_tests/shortcuts/click";

test("require email auth invalid email", async ({ browser }) => {
  const { form, page } = await preRequireEmail(browser);

  await (await getByID(form, "email")).fill("<>@<>");

  await getByTxt(page, "invalid email");
});

test("require email auth non existent email", async ({ browser }) => {
  const { form, page, payload } = await preRequireEmail(browser);

  await (await getByID(form, "email")).fill(payload.email);

  await clickByID(form, "conf_email__form__submit");

  await getByTxt(page, "user not found");
});
