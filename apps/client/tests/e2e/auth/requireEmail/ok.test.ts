import test from "@playwright/test";
import { preRequireEmail } from "./pre";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { getByID, getByTxt } from "../../lib_tests/shortcuts/get";
import { clickByID } from "../../lib_tests/shortcuts/click";
import { waitURL } from "../../lib_tests/shortcuts/wait";

test("require email auth ok", async ({ browser }) => {
  const { form, page, payload } = await preRequireEmail(browser, true);

  await (await getByID(form, "email")).fill(payload.email);

  await clickByID(form, "conf_email__form__submit");

  await waitURL(page, "/notice");

  await getByTxt(page, genMailNoticeMsg("to confirm the account"));

  await getByTxt(page, "email sent");
});
