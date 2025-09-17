import { Browser } from "@playwright/test";
import { clickByID, getByID, getUser2FA, preTest } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

export const preLogin2FA = async (browser: Browser) => {
  const { payload, ...rst } = await getUser2FA(browser, {});

  const page = await preTest(browser, "/auth/login");

  const formLogin = await getByID(page, "login__form");

  await (await getByID(formLogin, "email")).fill(payload.email);
  await (await getByID(formLogin, "password")).fill(payload.password);

  await clickByID(formLogin, "login__form__submit");

  await waitURL(page, "/auth/login-2FA");

  return {
    ...rst,
    payload,
    page,
  };
};
