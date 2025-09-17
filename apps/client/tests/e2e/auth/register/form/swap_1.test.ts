import test, { expect } from "@playwright/test";
import { getByID, getByTxt } from "../../../lib_tests/shortcuts/get";
import { checkTxtList } from "../../../lib_tests/shortcuts/check";
import { clickByID } from "../../../lib_tests/shortcuts/click";
import { checkTxtOpc } from "../../../lib_tests/shortcuts/style";
import { preAuthRegister } from "../pre";

test("register form swap 1", async ({ browser }) => {
  const { form, page } = await preAuthRegister(browser);

  await clickByID(form, "btns_swapper_next_swap");

  const pwd = await getByID(form, "password");
  await expect(pwd).toBeFocused();

  const confPwd = await getByID(form, "confirm_password");

  const msgs = ["invalid password", "you must confirm password"];

  await pwd.fill("abc");
  await confPwd.fill("");

  await checkTxtList(page, msgs);

  await confPwd.fill("123");

  await getByTxt(page, "passwords do not match");

  await clickByID(form, "body__form_terms");
  await clickByID(form, "body__form_terms");

  const msg = "you must accept terms & conditions";

  await getByTxt(page, msg);

  await clickByID(form, "body__form_terms");

  await checkTxtOpc(page, msg);

  const btn = await getByID(page, "pwd_generator__btn");

  await btn.hover();

  await getByTxt(page, "generate password");

  await btn.click();

  const resGen = await getByID(form, "pwd_generator__result");

  await resGen.click();

  await expect(page.getByText("copied to clipboard")).toBeAttached();

  const content = await (
    await getByID(resGen, "cpy_paste__result")
  ).evaluate((x) => x.textContent);

  await pwd.fill(content!);
  await confPwd.fill(content!);

  await checkTxtOpc(page, msgs[0]);
  await checkTxtOpc(page, "passwords do not match");

  await clickByID(form, "form_field_pwd__toggle_password");

  await expect(pwd).toHaveAttribute("type", "text");

  await clickByID(form, "form_field_pwd__toggle_confirm_password");

  await expect(pwd).toHaveAttribute("type", "password");
  await expect(confPwd).toHaveAttribute("type", "text");
});
