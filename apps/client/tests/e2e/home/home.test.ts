import test from "@playwright/test";
import { getByTxt, preTest } from "../lib_tests/idx";

test("should see home", async ({ browser }) => {
  const page = await preTest(browser, "/");

  await getByTxt(page, "script worked ✌🏽");
});
