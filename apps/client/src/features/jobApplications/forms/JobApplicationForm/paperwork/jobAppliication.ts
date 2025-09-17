import { REG_DATE_PICKER, REG_JOB_NAME, REG_TXT } from "@/core/constants/regex";
import { getDefValDatePicker } from "@/core/lib/dataStructure/formatters";
import { ApplicationStatusT } from "@/features/jobApplications/types";
import z from "zod";

export const addJobApplicationSchema = z.object({
  company_name: z
    .string()
    .min(1, "Company name required")
    .max(100, "Max length exceeded")
    .regex(REG_JOB_NAME, "Invalid company name"),

  position_name: z
    .string()
    .min(1, "Position name required")
    .max(100, "Max length exceeded")
    .regex(REG_JOB_NAME, "Invalid position name"),

  notes: z
    .string()
    .regex(REG_TXT, "Invalid notes characters")
    .max(1000, "Max length exceeded")
    .optional(),

  applied_at: z
    .string()
    .regex(REG_DATE_PICKER, "Invalid date format")
    .refine((v) => {
      const d = new Date(v);

      return !isNaN(d.getTime()) && d.toISOString().split("T")[0] === v;
    }, "Invalid date"),

  status: z
    .preprocess(
      (val) => (!val ? undefined : val),
      z
        .enum(Object.values(ApplicationStatusT), {
          error: "Invalid application status",
        })
        .optional()
    )
    .refine((v) => !!v, { message: "Application status required" }),
});

export type JobApplicationFormT = z.infer<typeof addJobApplicationSchema>;

export const resetValsJobApplForm = {
  company_name: "",
  position_name: "",
  notes: "",
  applied_at: getDefValDatePicker(),
  status: "" as ApplicationStatusT,
} as JobApplicationFormT;
