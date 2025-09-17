/* eslint-disable @typescript-eslint/no-explicit-any */
import { REG_JOB_NAME } from "@/core/constants/regex";
import { parseDevValUsFriendly } from "@/core/lib/dataStructure/formatters";
import { MapperArrayFieldsT, txtFieldSchema } from "@/core/paperwork";
import z from "zod";
import { searchJobsFieldsTxt } from "../uiFactory/search";
import { ApplicationStatusT } from "../../../types";
import { isStr } from "@/core/lib/dataStructure/ect";

const mapper: MapperArrayFieldsT = {
  company_name: {
    reg: REG_JOB_NAME,
    max: 100,
  },
  position_name: {
    reg: REG_JOB_NAME,
    max: 100,
  },
};

export const searchJobsSchema = z
  .object({
    txtFields: z.array(txtFieldSchema),

    status: z.array(z.enum(Object.values(ApplicationStatusT))),

    created_at_sort: z.string(),
    updated_at_sort: z.string(),
    applied_at_sort: z.string(),
  })
  .superRefine((data, ctx) => {
    let i = 0;

    while (i < data.txtFields.length) {
      const curr = data.txtFields?.[i];
      const friendlyName = parseDevValUsFriendly(curr.name, {});

      if (curr.val.trim().length)
        if (!mapper[curr.name as keyof typeof mapper].reg.test(curr.val))
          ctx.addIssue({
            code: "custom",
            message: `Invalid ${friendlyName}`,
            path: [`txtFields.${i}.val`],
          });
        else if (curr.val.length > mapper[curr.name as keyof typeof mapper].max)
          ctx.addIssue({
            code: "custom",
            message: `${friendlyName} length exceeded`,
            path: [`txtFields.${i}.val`],
          });

      i++;
    }

    const targetFields = Object.entries(data).filter(
      ([k, v]) =>
        k.includes("_sort") && isStr(v) && !["ASC", "DESC"].includes(v as any)
    );

    if (targetFields.length)
      for (const f of targetFields)
        ctx.addIssue({
          code: "custom",
          message: "Invalid Sort Value",
          path: [f[0]],
        });
  });

export type SearchJobsFormT = z.infer<typeof searchJobsSchema>;

export const resetValsSearchJobs: SearchJobsFormT = {
  txtFields: [{ ...searchJobsFieldsTxt[0], val: "" }],
  status: [],

  created_at_sort: "",
  updated_at_sort: "",
  applied_at_sort: "",
};
