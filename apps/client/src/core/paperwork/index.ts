import z from "zod";
import { REG_BACKUP_CODE, REG_PWD, REG_TOTP_CODE } from "../constants/regex";

export const emailSchema = z.object({
  email: z
    .email({ message: "Invalid email" })
    .min(1, "Email is required")
    .max(254, "Max length exceed"),
});

export type EmailFormT = z.infer<typeof emailSchema>;

export const resetValsEmailForm: EmailFormT = {
  email: "",
};

export const pwdSchema = z.object({
  password: z
    .string({ error: "Password required" })
    .min(1, "Password required")
    .max(100, "Max length exceeded")
    .regex(REG_PWD, "Invalid password"),
});

export type PwdFormT = z.infer<typeof pwdSchema>;

export const resetValsPwdForm: PwdFormT = {
  password: "",
};

export const pwdsSchema = z
  .object({
    confirm_password: z
      .string({ error: "You must confirm password" })
      .min(1, "You must confirm password"),
  })
  .and(pwdSchema)
  .refine((d) => d.password === d.confirm_password, {
    path: ["confirm_password"],
    message: "Passwords do not match",
  });

export type PwdsFormT = z.infer<typeof pwdsSchema>;

export const resetValsPwdsForm: PwdsFormT = {
  password: "",
  confirm_password: "",
};

export const schemaTotpCode = z
  .object({
    totp_code: z.array(z.string()).length(6, "Invalid Totp code length"),
  })
  .refine((data) => REG_TOTP_CODE.test(data.totp_code.join("")), {
    message: "Invalid Totp Code",
    path: ["totp_code"],
  });

export type ToptFormT = z.infer<typeof schemaTotpCode>;

export const resetValsTotpForm: ToptFormT = {
  totp_code: Array.from({ length: 6 }, () => ""),
};

export const schemaBackupForm = z.object({
  backup_code: z
    .string()
    .min(1, "Backup code required")
    .regex(REG_BACKUP_CODE, "Invalid backup code"),
});

export type BackupCodeFormT = z.infer<typeof schemaBackupForm>;

export const resetValsBackupForm: BackupCodeFormT = {
  backup_code: "",
};

export type ParamsAPI2FA = {
  cbc_hmac_token: string;
  totp_code?: string;
  backup_code?: string;
};

export const txtFieldSchema = z.object({
  id: z.string(),
  name: z.string(),
  type: z.string(),
  label: z.string(),
  place: z.string(),
  val: z.string(),
});

export interface MapperArrayFieldsT {
  [key: string]: {
    reg: RegExp;
    max: number;
  };
}
