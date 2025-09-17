import test, { expect } from "@playwright/test";
import { clickByID, getByID, getByTxt, preTest } from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";
import { preJobApplRead } from "./pre";

test("read job appl non logged user err", async ({ browser }) => {
  const page = await preTest(browser, "/job-applications/read");

  await waitURL(page, "/auth/login");
});

test("read job appl forbidden char err", async ({ browser }) => {
  const { searchBar, spanHits, page } = await preJobApplRead(browser);

  await (
    await getByID(searchBar, "primary_row__company_name")
  ).fill("<><>forbidden");

  await getByTxt(page, "invalid company name");

  await expect(await spanHits.innerText()).toBe("5");

  await clickByID(searchBar, "tertiary_row__search");

  await expect(await spanHits.innerText()).toBe("5");
});
