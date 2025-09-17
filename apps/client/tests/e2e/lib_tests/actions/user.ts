import { Browser } from "@playwright/test";
import { getTokensLib, getUser2FA } from "./fullActions";
import { goPage } from "../shortcuts/go";
import { waitTmr, waitURL } from "../shortcuts/wait";
import {
  clickByID,
  getByID,
  getByTxt,
  isToastOk,
  submitFormTOTP,
} from "../idx";
import { faker } from "@faker-js/faker";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { TokenT } from "@/common/types/tokens";

export const getAccessManageAcc = async (browser: Browser) => {
  const { payload, page } = await getTokensLib(browser, {});

  await goPage(page, "/user/manage-account");

  await waitURL(page, "/user/access-manage-account");

  const form = await getByID(page, "manage_acc__form");

  const pwd = await getByID(form, "password");

  await pwd.fill(payload.password);
  await clickByID(form, "manage_acc__form__submit");

  await waitURL(page, "/user/manage-account");

  await isToastOk(page);

  const container = await getByID(page, "manage_acc__form");

  return {
    payload,
    page,
    container,
  };
};

export const changeEmailOk = async (browser: Browser) => {
  const { page, container, payload } = await getAccessManageAcc(browser);

  const newEmail = faker.internet.email();

  const form = await getByID(container, "change_email__form");
  await (await getByID(form, "email")).fill(newEmail);

  await clickByID(form, "change_email__form__submit");

  await waitURL(page, "/notice");

  await isToastOk(page, "email sent to new address");

  await getByTxt(page, genMailNoticeMsg("to change your email address"));

  return {
    payload,
  };
};

export const getAccessManageAccVerified = async (browser: Browser) => {
  const { page, payload, ...rst } = await getTokensLib(browser, {
    tokenType: TokenT.MANAGE_ACC,
    verifyUser: true,
  });

  await page.goto("/user/manage-account");
  await waitURL(page, "/user/access-manage-account");

  const form = await getByID(page, "manage_acc__form");

  await (await getByID(form, "password")).fill(payload.password);
  await clickByID(form, "manage_acc__form__submit");
  await waitURL(page, "/user/manage-account");

  await isToastOk(page);

  for (let i = 0; i < 2; i++) {
    await clickByID(page, "btns_swapper_next_swap");
    await waitTmr(page);
  }

  const swap = await getByID(page, "setup_2FA__swap");

  await getByTxt(swap, "Setup 2FA with TOTP code");

  return {
    ...rst,
    page,
    payload,
    swap,
  };
};

export const goFormPreManageAcc2FA = async (browser: Browser) => {
  const { page, payload, ...rst } = await getUser2FA(browser, {});

  await page.goto("/user/manage-account");

  await waitURL(page, "/user/access-manage-account");

  const form = await getByID(page, "manage_acc__form");

  await (await getByID(form, "password")).fill(payload.password);

  await clickByID(form, "manage_acc__form__submit");

  await waitURL(page, "/user/access-manage-account-2FA");

  const form2FA = await getByID(page, "2FA__form");

  return {
    ...rst,
    page,
    payload,
    form2FA,
  };
};

export const getAccessManageAcc2FA = async (browser: Browser) => {
  const { page, totp_secret, ...rst } = await goFormPreManageAcc2FA(browser);

  await submitFormTOTP(page, { totp_secret, url: "/user/manage-account" });

  const form = await getByID(page, "manage_acc__form");

  return {
    ...rst,
    page,
    totp_secret,
    form,
  };
};
