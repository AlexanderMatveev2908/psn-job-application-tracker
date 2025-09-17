import test from "@playwright/test";
import { getTokensLib } from "../../lib_tests/actions/fullActions";
import { getByID, getByTxt } from "../../lib_tests/shortcuts/get";
import { closeToast } from "../../lib_tests/actions/sideActions";
import { clickByID } from "../../lib_tests/shortcuts/click";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { waitURL } from "../../lib_tests/shortcuts/wait";

test("require email user ok", async ({ browser }) => {
  const { page, payload } = await getTokensLib(browser, {});

  await page.reload();

  await closeToast(page);

  const drop = await getByID(page, "header__toggle_drop");

  await drop.click();

  await clickByID(page, "header_link__confirm_email");

  await waitURL(page, "/user/require-email/confirm-email");

  const form = await getByID(page, "conf_email__form");

  await (await getByID(form, "email")).fill(payload.email + "abcd");

  await clickByID(form, "conf_email__form__submit");

  await getByTxt(page, "email different from one declared at register time");

  await (await getByID(form, "email")).fill(payload.email);

  await clickByID(form, "conf_email__form__submit");

  await waitURL(page, "/notice");

  await getByTxt(page, genMailNoticeMsg("to confirm the account"));
});
