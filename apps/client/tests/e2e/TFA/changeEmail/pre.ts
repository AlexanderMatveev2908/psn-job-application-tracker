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
  const { form, page, payload, ...rst } = await getAccessManageAcc2FA(browser);

  const formEmail = await getByID(form, "change_email__form");

  const newEmail = faker.internet.email();

  await (await getByID(formEmail, "email")).fill(newEmail);

  await clickByID(formEmail, "change_email__form__submit");

  await waitURL(page, "/notice");
  await isToastOk(page);

  const { cbc_hmac_token } = await getTokensLib(browser, {
    payload,
    tokenType: TokenT.CHANGE_EMAIL,
  });

  const pageCheck = await preTest(
    browser,
    `/verify?cbc_hmac_token=${cbc_hmac_token}`
  );

  return {
    ...rst,
    page: pageCheck,
  };
};
