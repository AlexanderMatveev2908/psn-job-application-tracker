import SvgConfirmEmail from "@/common/components/SVGs/ConfirmEmail";
import { GoHomeFill } from "react-icons/go";
import SvgRegister from "@/common/components/SVGs/Register";
import { LinkAppSvgT } from "@/common/types/ui";
import { FaKey, FaUserGear } from "react-icons/fa6";
import { FiLogIn } from "react-icons/fi";
import { HiOutlineLogout } from "react-icons/hi";

export const linksAll: LinkAppSvgT[] = [
  {
    label: "Home",
    href: "/",
    Svg: GoHomeFill,
  },
];

export const linkConfirmEmail: LinkAppSvgT = {
  label: "Confirm Email",
  href: "/auth/require-email/confirm-email",
  Svg: SvgConfirmEmail,
};
export const linkRegister: LinkAppSvgT = {
  label: "Register",
  href: "/auth/register",
  Svg: SvgRegister,
};
export const linkLogin: LinkAppSvgT = {
  label: "Login",
  href: "/auth/login",
  Svg: FiLogIn,
};
export const linkRecoverPwd: LinkAppSvgT = {
  label: "Recover Password",
  href: "/auth/require-email/recover-password",
  Svg: FaKey,
  fill: "var(--white__0)",
};

export const linksNonLoggedAccount: LinkAppSvgT[] = [
  linkRegister,
  linkLogin,
  linkRecoverPwd,
  linkConfirmEmail,
];

export const linkConfEmailLogged = {
  label: "Confirm Email",
  href: "/user/require-email/confirm-email",
  Svg: SvgConfirmEmail,
};

export const linksLoggedAccount = [
  linkConfEmailLogged,
  {
    label: "Manage Account",
    href: "/user/manage-account",
    Svg: FaUserGear,
  },
];

export const linkLogout: LinkAppSvgT = {
  label: "Logout",
  href: "#",
  Svg: HiOutlineLogout,
};
