import test from "@playwright/test";
import { preAuthNavigation } from "./pre";
import {
  checkLinksList,
  clickByID,
  clickByTxt,
  getByID,
} from "../../lib_tests/idx";
import { waitURL } from "../../lib_tests/shortcuts/wait";

test("navigation to register page with sidebar", async ({ browser }) => {
  const { page } = await preAuthNavigation(browser);

  await clickByID(page, "header__toggle_sidebar");

  const el = await getByID(page, "sidebar");

  await checkLinksList(el, ["Home", "Applications", "Add application"]);

  await clickByID(el, "drop_menu_static__btn_toggle");

  await checkLinksList(el, [
    "Home",
    "Register",
    "Login",
    "Recover Password",
    "Confirm Email",
  ]);

  await clickByTxt(el, "register");

  await waitURL(page, "/auth/register");
});
