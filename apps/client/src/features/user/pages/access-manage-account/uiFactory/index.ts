import { PwdFormT } from "@/core/paperwork";
import { FormFieldGen } from "@/core/uiFactory/classes";

const gen = new FormFieldGen<PwdFormT>();

export const pwdField = gen.txtField({
  name: "password",
});
