import test, { expect } from "@playwright/test";
import { preJobAppl } from "../pre";
import { goPage } from "../../lib_tests/shortcuts/go";
import { clickByID, getByID, getByTxt } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";

test("del appl ok", async ({ browser }) => {
  const { page, job_application } = await preJobAppl(browser);

  await goPage(page, "/job-applications/read");

  const card = await getByID(page, "job_appl__card");

  await expect((await card.all()).length).toBe(1);

  await clickByID(card, "btn__del_appl");

  const popup = await getByID(page, "popup__del_application");

  await getByTxt(
    popup,
    `Delete application for ${job_application.company_name}`
  );

  await clickByID(popup, "pop__confirm__btn");

  await waitTmr(page, 10000);

  const spanHits = await getByID(page, "search_bar__n_hits");

  await expect(await spanHits.innerText()).toBe("0");

  await expect((await card.all()).length).toBe(0);
});
