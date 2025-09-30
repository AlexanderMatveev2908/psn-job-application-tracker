import { REG_NAME } from "@/core/constants/regex";
import { emailSchema, pwdsSchema } from "@/core/paperwork";
import { z } from "zod";
import {
  myMail,
  myPwd,
  wrapGetValsFormManualTest,
} from "@/features/auth/lib/etc";

export const registerSchema = emailSchema
  .extend({
    firstName: z
      .string()
      .min(1, "First Name is required")
      .max(100, "Max length exceeded")
      .regex(REG_NAME, "Invalid characters"),
    lastName: z
      .string()
      .min(1, "Last Name is required")
      .max(100, "Max length exceeded")
      .regex(REG_NAME, "Invalid characters"),

    terms: z
      .boolean()
      .refine((v) => v, { message: "You must accept terms & conditions" }),
  })
  .and(pwdsSchema);

export type RegisterFormT = z.infer<typeof registerSchema>;

export const resetValsRegister: RegisterFormT = {
  firstName: "",
  lastName: "",
  email: "",
  password: "",
  confirmPassword: "",
  terms: false,
};

export const getDefValsRegister = (): RegisterFormT =>
  wrapGetValsFormManualTest(resetValsRegister, {
    firstName: "Alex",
    lastName: "Matveev",
    email: myMail,
    password: myPwd,
    confirmPassword: myPwd,
    terms: true,
  });
