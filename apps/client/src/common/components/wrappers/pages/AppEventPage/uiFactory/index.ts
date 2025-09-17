import { AppEventT } from "@/common/types/api";
import { Badge, CircleAlert, CircleCheckBig, ShieldAlert } from "lucide-react";
import { IconType } from "react-icons";
import { FaCircleQuestion } from "react-icons/fa6";

export const SvgsAppEvents: Record<AppEventT, IconType> = {
  OK: CircleCheckBig,
  INFO: FaCircleQuestion,
  ERR: CircleAlert,
  WARN: ShieldAlert,
  NONE: Badge,
};
