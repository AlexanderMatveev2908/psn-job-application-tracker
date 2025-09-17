import { parseDevValUsFriendly } from "@/core/lib/dataStructure/formatters";
import { FormFieldGen } from "@/core/uiFactory/classes";
import { ApplicationStatusT } from "../types";

const gen = new FormFieldGen();

export const companyNameField = gen.txtField({ name: "company_name" });
export const positionNameField = gen.txtField({ name: "position_name" });

export const applicationStatusChoices = Object.values(ApplicationStatusT).map(
  (v) => ({
    val: v,
    label: parseDevValUsFriendly(v, { titleCase: true }),
  })
);
