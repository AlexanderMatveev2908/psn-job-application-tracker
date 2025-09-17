import { ApplicationStatusT } from "@/features/jobApplications/types";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";
import test, { expect } from "@playwright/test";
import { preJobApplRead } from "./pre";

test("read job appl filter by status checkbox ok", async ({ browser }) => {
  const { searchBar, applications, page } = await preJobApplRead(browser);

  await clickByID(searchBar, "search_bar__btn__filterBar");

  const filterBar = await getByID(page, "search_bar__filter_bar");
  const filterBarBodyVals = await getByID(
    await getByID(filterBar, "filter_bar__body"),
    "body__vals"
  );

  await clickByID(filterBar, "search_bar__btn__close_filter_bar");

  for (const status of Object.values(ApplicationStatusT)) {
    await clickByID(searchBar, "tertiary_row__reset");

    await clickByID(searchBar, "search_bar__btn__filterBar");

    await clickByID(filterBarBodyVals, `body__vals__${status}`);

    await clickByID(filterBar, "search_bar__btn__close_filter_bar");

    await waitTmr(page, 10000);

    const cardsByStatus = await page.getByTestId("job_appl__card");
    const countByStatus = await cardsByStatus.count();

    await expect(countByStatus).toBe(
      applications.filter((el) => el.status === status).length
    );
  }
});
