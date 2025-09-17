import test, { expect } from "@playwright/test";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";
import { preJobApplRead } from "./pre";

test("read job appl sort by applied_at ok", async ({ browser }) => {
  const { searchBar, applications, page, spanHits } = await preJobApplRead(
    browser
  );

  await clickByID(searchBar, "search_bar__btn__sortBar");

  const sortBar = await getByID(page, "search_bar__sort_bar");

  await clickByID(sortBar, "sort_bar__applied_at_sort__ASC");

  await clickByID(sortBar, "btn__close_popup");

  await waitTmr(page, 10000);

  const cardsSortedByApplDate = await page.getByTestId("job_appl__card");
  await expect(await spanHits.innerText()).toBe("5");

  const sorted = applications.sort((a, b) => a.applied_at - b.applied_at);

  let i = 0;
  while (i < 5) {
    const curr = await cardsSortedByApplDate.nth(i);

    await expect(
      await (await getByID(curr, "card__company_name")).innerText()
    ).toBe(sorted[i].company_name);

    await expect(
      await (await getByID(curr, "card__position_name")).innerText()
    ).toBe(sorted[i].position_name);

    await expect(await (await getByID(curr, "card__status")).innerText()).toBe(
      sorted[i].status
    );

    i++;
  }
});
