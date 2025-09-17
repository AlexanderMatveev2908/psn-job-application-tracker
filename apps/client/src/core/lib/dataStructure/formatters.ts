import { UserT } from "@/features/user/types";

export const formatDateDev = (date: Date | string | number) => {
  const param =
    date instanceof Date
      ? date
      : /^\d{10,}n?$/.test(date + "")
      ? +date
      : new Date(date);

  return new Intl.DateTimeFormat("en-US", {
    hour: "numeric",
    minute: "numeric",
    second: "numeric",

    hour12: true,
  }).format(param);
};

export const capt = (str?: string) =>
  str ? str?.[0]?.toUpperCase() + str?.slice(1) : "";

export const captAll = (str?: string) =>
  !str
    ? ""
    : str
        .split(" ")
        .filter(Boolean)
        .map((el) => capt(el))
        .join(" ");

export const extractInitialsUser = (user: UserT) =>
  user.first_name[0].toUpperCase() + user.last_name[0].toUpperCase();

export const parseDevValUsFriendly = (
  v: string,
  { titleCase }: { titleCase?: boolean }
) => {
  const res = v.split("_").join(" ").toLowerCase();

  return titleCase ? captAll(res) : res;
};

export const getDefValDatePicker = () => new Date().toISOString().split("T")[0];

export const fromPickerToTmst = (v: string) =>
  new Date(v + "T00:00:00Z").getTime();

export const formatDate = (val: number) =>
  new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  }).format(new Date(val));
