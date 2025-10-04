import { Browser } from "@playwright/test";
import {
  genRegisterPayload,
  PayloadRegisterT,
} from "../../lib_tests/conf/payloads";
import { preTest } from "../../lib_tests/conf/pre";
import { getByID } from "../../lib_tests/shortcuts/get";
import { registerUserOk } from "../../lib_tests/actions/auth";

export const preRequireEmail = async (
  browser: Browser,
  mustExists?: boolean
) => {
  let payload: PayloadRegisterT;
  if (mustExists) payload = (await registerUserOk(browser)).payload;
  else payload = genRegisterPayload();

  const page = await preTest(browser, "/auth/require-email/confirm-email");

  const form = await getByID(page, "conf_email__form");

  return {
    payload,
    page,
    form,
  };
};
