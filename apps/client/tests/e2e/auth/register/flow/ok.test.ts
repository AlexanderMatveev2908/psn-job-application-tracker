import test from "@playwright/test";
import { registerUserOk } from "../../../lib_tests/actions/auth";

test("register ok", async ({ browser }) => {
  await registerUserOk(browser);
});
