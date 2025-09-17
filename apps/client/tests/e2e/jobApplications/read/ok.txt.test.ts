import test, { expect } from "@playwright/test";
import { preJobApplRead } from "./pre";
import { clickByID, getByID } from "../../lib_tests/idx";
import { waitTmr } from "../../lib_tests/shortcuts/wait";

test("read job appl filter by text inputs ok", async ({ browser }) => {
  const { searchBar, applications, page } = await preJobApplRead(browser);

  const [firstAppl] = applications;

  await (
    await getByID(searchBar, "primary_row__company_name")
  ).fill(firstAppl.company_name);

  const rowDrop = await getByID(searchBar, "search_bar__drop_row");
  await clickByID(rowDrop, "drop_row__btn");

  await waitTmr(page);

  const drop = await getByID(rowDrop, "drop_row__txt_fields");

  await clickByID(drop, `txt_fields__position_name`);

  const positionNameField = await getByID(
    searchBar,
    "primary_row__position_name"
  );

  await positionNameField.fill(firstAppl.position_name);

  await waitTmr(page, 10000);

  const cardsByTxt = await page.getByTestId("job_appl__card");
  const countByTxt = await cardsByTxt.count();

  await expect(countByTxt >= 1).toBeTruthy();

  let found = false;
  for (const c of await cardsByTxt.all()) {
    try {
      const companyName = await getByID(c, "card__company_name");
      await expect(firstAppl.company_name).toBe(await companyName.innerText());
      const positionName = await getByID(c, "card__position_name");
      await expect(firstAppl.position_name).toBe(
        await positionName.innerText()
      );

      found = true;
      break;
    } catch {}
  }

  await expect(found).toBe(true);
});
