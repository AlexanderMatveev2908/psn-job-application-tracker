import { genMailNoticeMsg } from "@/core/constants/etc";
import { preAuthRegister } from "../../auth/register/pre";
import { clickByID } from "../shortcuts/click";
import { getByID, getByTxt } from "../shortcuts/get";
import { waitURL } from "../shortcuts/wait";
import { preAuthLogin } from "../../auth/login/pre";
import { Browser, expect, Page } from "@playwright/test";
import { genRegisterPayload, PayloadRegisterT } from "../conf/payloads";
import { genPwd } from "@/core/lib/pwd";
import { REG_JWT } from "@/core/constants/regex";

export const registerUserOk = async (
  browser: Browser,
  payload: PayloadRegisterT = genRegisterPayload()
) => {
  const { form, page } = await preAuthRegister(browser);

  const firstName = await getByID(form, "first_name");
  firstName.fill(payload.first_name);

  const lastName = await getByID(form, "last_name");
  lastName.fill(payload.last_name);

  const email = await getByID(form, "email");
  email.fill(payload.email);

  await clickByID(form, "btns_swapper_next_swap");

  const pwd = await getByID(form, "password");
  await pwd.fill(payload.password);

  const confPwd = await getByID(form, "confirm_password");
  confPwd.fill(payload.password);

  await clickByID(form, "body__form_terms");

  await clickByID(form, "register__form__submit");

  await waitURL(page, "/notice");

  await getByTxt(page, genMailNoticeMsg("to confirm the account"));

  return {
    payload,
  };
};

export const loginUserOk = async (browser: Browser) => {
  const payload = genRegisterPayload();

  await registerUserOk(browser, payload);

  const { page, form } = await preAuthLogin(browser);

  const email = await getByID(form, "email");
  email.fill(payload.email);

  const pwd = await getByID(form, "password");
  pwd.fill(payload.password);

  await clickByID(form, "login__form__submit");

  await waitURL(page, "/");

  await getByTxt(page, "operation successful");

  return {
    payload,
    page,
  };
};

export const recoverPwdOk = async (page: Page) => {
  const form = await getByID(page, "recover_pwd__form");

  const newPwd = genPwd();
  await (await getByID(form, "password")).fill(newPwd);
  await (await getByID(form, "confirm_password")).fill(newPwd);

  await clickByID(form, "recover_pwd__form__submit");

  await waitURL(page, "/");

  const jwt = await page.evaluate(() => sessionStorage.getItem("access_token"));
  await expect(REG_JWT.test(jwt ?? "")).toBeTruthy();
};
