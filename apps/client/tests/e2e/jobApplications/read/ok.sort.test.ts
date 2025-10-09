import test, { expect } from "@playwright/test";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";
import { preJobApplRead } from "./pre";

test("read job appl sort by appliedAt ok", async ({ browser }) => {
  const { searchBar, applications, page, spanHits } = await preJobApplRead(
    browser
  );

  await clickByID(searchBar, "search_bar__btn__sortBar");

  const sortBar = await getByID(page, "search_bar__sort_bar");

  await clickByID(sortBar, "sort_bar__appliedAt_sort__ASC");

  await clickByID(sortBar, "btn__close_popup");

  await waitTmr(page, 10000);

  const cardsSortedByApplDate = await page.getByTestId("job_appl__card");
  await expect(await spanHits.innerText()).toBe("5");

  const sorted = applications.sort((a, b) => a.appliedAt - b.appliedAt);

  let i = 0;
  while (i < 5) {
    const curr = await cardsSortedByApplDate.nth(i);

    await expect(
      await (await getByID(curr, "card__companyName")).innerText()
    ).toBe(sorted[i].companyName);

    await expect(
      await (await getByID(curr, "card__positionName")).innerText()
    ).toBe(sorted[i].positionName);

    await expect(await (await getByID(curr, "card__status")).innerText()).toBe(
      sorted[i].status
    );

    i++;
  }
});
