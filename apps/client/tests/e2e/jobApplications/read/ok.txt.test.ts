import test, { expect } from "@playwright/test";
import { preJobApplRead } from "./pre";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";

test("read job appl filter by text inputs ok", async ({ browser }) => {
  const { searchBar, applications, page } = await preJobApplRead(browser);

  const [firstAppl] = applications;

  await (
    await getByID(searchBar, "primary_row__companyName")
  ).fill(firstAppl.companyName);

  const rowDrop = await getByID(searchBar, "search_bar__drop_row");
  await clickByID(rowDrop, "drop_row__btn");

  await waitTmr(page);

  const drop = await getByID(rowDrop, "drop_row__txt_fields");

  await clickByID(drop, `txt_fields__positionName`);

  const positionNameField = await getByID(
    searchBar,
    "primary_row__positionName"
  );

  await positionNameField.fill(firstAppl.positionName);

  await waitTmr(page, 10000);

  const cardsByTxt = await page.getByTestId("job_appl__card");
  const countByTxt = await cardsByTxt.count();

  await expect(countByTxt >= 1).toBeTruthy();

  let found = false;
  for (const c of await cardsByTxt.all()) {
    try {
      const companyName = await getByID(c, "card__companyName");
      await expect(firstAppl.companyName).toBe(await companyName.innerText());
      const positionName = await getByID(c, "card__positionName");
      await expect(firstAppl.positionName).toBe(await positionName.innerText());

      found = true;
      break;
    } catch {}
  }

  await expect(found).toBe(true);
});
