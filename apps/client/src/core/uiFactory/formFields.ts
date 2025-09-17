/* eslint-disable @typescript-eslint/no-explicit-any */
import { FormFieldTxtT } from "@/common/types/ui";
import { FormFieldGen } from "@/core/uiFactory/classes";
import { EmailFormT } from "../paperwork";

const gen = new FormFieldGen<any>();

export const pwdField = gen.txtField({
  name: "password",
});

export type PwdT = "password" | "confirm_password";

export const pwdFields: Record<PwdT, FormFieldTxtT<any>> = {
  password: gen.txtField({ name: "password" }),
  confirm_password: gen.txtField({ name: "confirm_password" }),
};

export const emailField = gen.txtField({
  name: "email",
  type: "email",
}) as FormFieldTxtT<EmailFormT>;
