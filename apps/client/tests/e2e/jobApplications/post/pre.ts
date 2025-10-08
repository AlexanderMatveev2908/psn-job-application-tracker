import { Browser } from "@playwright/test";
import { genPayloadJobAppl, getByID, getTokensLib } from "../../lib_tests/idx";
import { goPage } from "../../lib_tests/shortcuts/go";

export const preJobAppl = async (browser: Browser) => {
  const { page, ...rst } = await getTokensLib(browser, {});

  await goPage(page, "/job-applications/post");

  const form = await getByID(page, "job_application__form");

  const payload = genPayloadJobAppl();

  return {
    ...rst,
    page,
    payload,
    form,
  };
};
