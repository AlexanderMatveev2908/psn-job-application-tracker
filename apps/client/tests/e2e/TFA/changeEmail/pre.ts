import { Browser } from "@playwright/test";
import { getAccessManageAcc2FA } from "../../lib_tests/actions/user";
import {
  clickByID,
  getByID,
  getTokensLib,
  isToastOk,
  preTest,
} from "../../lib_tests/idx";
import { faker } from "@faker-js/faker";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { TokenT } from "@/common/types/tokens";

export const preChangeEmail2FA = async (browser: Browser) => {
  const { form, page, user, ...rst } = await getAccessManageAcc2FA(browser);

  const formEmail = await getByID(form, "change_email__form");

  const newEmail = faker.internet.email();

  await (await getByID(formEmail, "email")).fill(newEmail);

  await clickByID(formEmail, "change_email__form__submit");

  await waitURL(page, "/notice");
  await isToastOk(page);

  const { cbcHmacToken } = await getTokensLib(browser, {
    payload: user,
    tokenType: TokenT.CHANGE_EMAIL,
  });

  const pageCheck = await preTest(
    browser,
    `/verify?cbcHmacToken=${cbcHmacToken}`
  );

  return {
    ...rst,
    page: pageCheck,
  };
};
