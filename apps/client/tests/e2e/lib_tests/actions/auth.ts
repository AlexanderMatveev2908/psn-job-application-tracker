import { genMailNoticeMsg } from "@/core/constants/etc";
import { preAuthRegister } from "../../auth/register/pre";
import { clickByID } from "../shortcuts/click";
import { getByID, getByTxt } from "../shortcuts/get";
import { waitURL } from "../shortcuts/wait";
import { Browser } from "@playwright/test";
import { genRegisterPayload, PayloadRegisterT } from "../conf/payloads";

export const registerUserOk = async (
  browser: Browser,
  payload: PayloadRegisterT = genRegisterPayload()
) => {
  const { form, page } = await preAuthRegister(browser);

  const firstName = await getByID(form, "firstName");
  firstName.fill(payload.firstName);

  const lastName = await getByID(form, "lastName");
  lastName.fill(payload.lastName);

  const email = await getByID(form, "email");
  email.fill(payload.email);

  await clickByID(form, "btns_swapper_next_swap");

  const pwd = await getByID(form, "password");
  await pwd.fill(payload.password);

  const confPwd = await getByID(form, "confirmPassword");
  confPwd.fill(payload.password);

  await clickByID(form, "body__form_terms");

  await clickByID(form, "register__form__submit");

  await waitURL(page, "/notice");

  await getByTxt(page, genMailNoticeMsg("to confirm the account"));

  return {
    payload,
  };
};
