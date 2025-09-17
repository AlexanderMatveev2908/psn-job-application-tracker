/* eslint-disable @typescript-eslint/no-explicit-any */
import test, { expect } from "@playwright/test";
import { clickByID, genPayloadJobAppl, getByID } from "../../lib_tests/idx";
import { waitTmr, waitURL } from "../../lib_tests/shortcuts/wait";
import { preJobAppl, relevantKeysJobAppl } from "../pre";

test("put appl ok", async ({ browser }) => {
  const { originalPayload, page, job_application } = await preJobAppl(browser);

  await page.goto(`/job-applications/put/${job_application.id}`);

  const companyNameField = await getByID(page, "company_name");
  await expect(companyNameField).toHaveValue(job_application.company_name);

  const positionNameField = await getByID(page, "position_name");
  await expect(positionNameField).toHaveValue(job_application.position_name);

  const wrapBoxes = await getByID(page, "wrap_swap_boxes__application_status");
  const boxChosen = await getByID(
    wrapBoxes,
    `swap_boxes__${originalPayload.status}`
  );
  await expect(
    boxChosen.evaluate((el) => el.style.background === "var(--white__0)")
  ).toBeTruthy();

  const updatedPayload = genPayloadJobAppl();

  await companyNameField.fill(updatedPayload.company_name);
  await positionNameField.fill(updatedPayload.position_name);

  await clickByID(wrapBoxes, `swap_boxes__${updatedPayload.status}`);

  await clickByID(page, "job_application__form__submit");

  await waitURL(page, "/job-applications/read");
  await waitTmr(page, 10000);

  const cards = await page.getByTestId("job_appl__card");
  await expect((await cards.all()).length).toBe(1);

  const cardUpdated = await cards.nth(0);

  for (const k of relevantKeysJobAppl)
    await expect(
      await (await getByID(cardUpdated, `card__${k}`)).innerText()
    ).toBe((updatedPayload as any)[k]);
});
