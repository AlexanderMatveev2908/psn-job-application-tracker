import { FormFieldGen } from "@/core/uiFactory/classes";
import { ApplicationStatusT } from "../types";
import { parseDevValUsFriendly } from "@/core/lib/dataStructure/formatters";

const gen = new FormFieldGen();

export const companyNameField = gen.txtField({
  name: "companyName",
  label: "Company Name",
});
export const positionNameField = gen.txtField({
  name: "positionName",
  label: "Position Name",
});

export const applicationStatusChoices = Object.values(ApplicationStatusT).map(
  (v) => ({
    val: v,
    label: parseDevValUsFriendly(v, { titleCase: true }),
  })
);
