import test from "@playwright/test";
import { loginUserOk } from "../../lib_tests/actions/auth";

test("login ok", async ({ browser }) => {
  await loginUserOk(browser);
});
