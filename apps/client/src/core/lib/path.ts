import { ErrApp } from "./err";
import { captAll } from "./dataStructure/formatters";

export const calcIsCurrPath = (path: string, href: string) => {
  const noQuery = path.split(/[?#]/).shift();
  const base = noQuery!.replace(/\/+$/, "");

  const escaped = base.replace(/[.*+?^${}()|\\[\]]/g, "\\$&");

  const reg = new RegExp(`^${escaped}(?:/+)?(?:\\?.*)?$`);

  return reg.test(href);
};

export const extractTestIdPath = (p: string) => {
  const act = p.split("/").pop();

  if (!act) throw new ErrApp("invalid working path call");

  const parsed = act.replace(/-/g, "_").toLowerCase();

  return parsed;
};

export const extractNamePagePath = (
  p: string,
  { usFriendly }: { usFriendly: boolean }
) => {
  const act = p.split("/").pop();

  if (!act) throw new ErrApp("invalid working path call");

  return usFriendly ? captAll(act?.replace("-", " ")) : act;
};
