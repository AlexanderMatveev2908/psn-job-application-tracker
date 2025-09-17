import test from "@playwright/test";
import { checkTxtList } from "../../../lib_tests/shortcuts/check";
import { checkTxtListOpc } from "../../../lib_tests/shortcuts/style";
import { preAuthRegister } from "../pre";
import { waitTmr } from "../../../lib_tests/shortcuts/wait";

test("register form swap 0", async ({ browser }) => {
  const { form, page } = await preAuthRegister(browser);

  const firstName = form.getByTestId("first_name");
  const lastName = form.getByTestId("last_name");
  const email = form.getByTestId("email");

  await firstName.fill("<>!");
  await lastName.fill("...");
  await lastName.fill("");
  await email.fill("invalid@@@...");

  const msgs = ["invalid characters", "last name is required", "invalid email"];

  await checkTxtList(page, msgs);

  await firstName.fill("John");
  await lastName.fill("Doe");
  await email.fill("john@gmail.com");

  await waitTmr(page);

  await checkTxtListOpc(page, msgs);
});
