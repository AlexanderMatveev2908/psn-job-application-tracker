import test from "@playwright/test";
import { preAuthRecoverPwd } from "./pre";
import { clickByID, getByID, getByTxt } from "../../lib_tests/idx";

test("recover pwd err same pwd", async ({ browser }) => {
  const { page, form, plainPwd } = await preAuthRecoverPwd(browser);

  await (await getByID(form, "password")).fill(plainPwd);
  await (await getByID(form, "confirmPassword")).fill(plainPwd);

  await clickByID(form, "recover_pwd__form__submit");

  await getByTxt(page, "new password must be different from old one");
});
