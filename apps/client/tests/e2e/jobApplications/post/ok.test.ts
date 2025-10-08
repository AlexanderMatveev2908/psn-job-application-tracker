import test from "@playwright/test";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { preJobAppl } from "./pre";

test("post job appl ok", async ({ browser }) => {
  const { form, page, payload } = await preJobAppl(browser);

  await (await getByID(form, "companyName")).fill(payload.companyName);
  await (await getByID(form, "positionName")).fill(payload.positionName);

  const swapBoxes = await getByID(form, "wrap_swap_boxes__application_status");

  await clickByID(swapBoxes, `swap_boxes__${payload.status}`);

  await (await getByID(form, "appliedAt")).fill(payload.appliedAt);
  await (await getByID(form, "notes")).fill(payload.notes ?? "");

  await clickByID(form, "job_application__form__submit");

  await waitURL(page, "/job-applications/read");
});
