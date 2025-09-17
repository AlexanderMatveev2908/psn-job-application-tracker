import test from "@playwright/test";
import { clickByID, getByID, getByTxt, preTest } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { preJobAppl } from "./pre";
import { ApplicationStatusT } from "@/features/jobApplications/types";

test("err post job appl not logged", async ({ browser }) => {
  const page = await preTest(browser, "/job-applications/post");

  await waitURL(page, "/auth/login");
});

test("err post job appl generic", async ({ browser }) => {
  const { form, page } = await preJobAppl(browser);

  const wrongTxt = "<><>!@#";
  const inputs = ["company_name", "position_name", "notes"];

  for (const f of inputs) {
    await (await getByID(form, f)).fill(wrongTxt);

    await getByTxt(page, `invalid ${f.split("_").join(" ")}`);
  }

  const swapBoxes = await getByID(form, "wrap_swap_boxes__application_status");

  for (let i = 0; i < 2; i++) {
    await clickByID(swapBoxes, `swap_boxes__${ApplicationStatusT.APPLIED}`);
  }

  await getByTxt(page, "application status required");
});
