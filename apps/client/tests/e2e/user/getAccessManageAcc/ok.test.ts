import test from "@playwright/test";
import { getAccessManageAcc } from "../../lib_tests/actions/user";

test("get access mng acc ok", async ({ browser }) => {
  await getAccessManageAcc(browser);
});
