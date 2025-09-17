import test from "@playwright/test";
import { preAuthRecoverPwd } from "./pre";
import { recoverPwdOk } from "../../lib_tests/actions/auth";

test("recover pwd ok", async ({ browser }) => {
  const { page } = await preAuthRecoverPwd(browser);

  await recoverPwdOk(page);
});
