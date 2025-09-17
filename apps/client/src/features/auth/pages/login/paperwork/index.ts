import { emailSchema, pwdSchema } from "@/core/paperwork";
import {
  myMail,
  myPwd,
  wrapGetValsFormManualTest,
} from "@/features/auth/lib/etc";
import z from "zod";

export const loginSchema = emailSchema.and(pwdSchema);

export type LoginFormT = z.infer<typeof loginSchema>;

export const resetValsLogin: LoginFormT = {
  email: "",
  password: "",
};

export const getValsLogin = wrapGetValsFormManualTest(resetValsLogin, {
  email: myMail,
  password: myPwd,
});
