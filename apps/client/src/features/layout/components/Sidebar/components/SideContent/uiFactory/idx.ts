import SvgAccount from "@/common/components/SVGs/Account";
import SvgSearch from "@/common/components/SVGs/Search";
import SvgWrite from "@/common/components/SVGs/Write";
import { FieldTxtSvgT, LinkAppSvgT } from "@/common/types/ui";

export const sideLinksLogged: LinkAppSvgT[] = [
  {
    label: "Applications",
    href: "/job-applications/read",
    Svg: SvgSearch,
  },
  {
    label: "Add application",
    href: "/job-applications/post",
    Svg: SvgWrite,
  },
];

export const sideDropAccount: FieldTxtSvgT = {
  label: "Account",
  Svg: SvgAccount,
};
