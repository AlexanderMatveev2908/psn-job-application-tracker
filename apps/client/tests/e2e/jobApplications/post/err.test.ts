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
  const inputs = [
    { field: "companyName", label: "company name" },
    { field: "positionName", label: "position name" },
    { field: "notes", label: "notes" },
  ];

  for (const f of inputs) {
    await (await getByID(form, f.field)).fill(wrongTxt);

    await getByTxt(page, `invalid ${f.label}`);
  }

  const swapBoxes = await getByID(form, "wrap_swap_boxes__application_status");

  for (let i = 0; i < 2; i++) {
    await clickByID(swapBoxes, `swap_boxes__${ApplicationStatusT.APPLIED}`);
  }

  await getByTxt(page, "application status required");
});
