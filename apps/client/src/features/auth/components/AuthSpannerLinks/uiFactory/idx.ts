import { LinkAppSvgT } from "@/common/types/ui";
import {
  linkConfirmEmail,
  linkLogin,
  linkRecoverPwd,
  linkRegister,
} from "@/core/uiFactory/links";

export type AuthSpannerLinksT =
  | "register"
  | "login"
  | "confirm-email"
  | "recover-password";

export type AuthSpannerLinksObjT = Record<"msg" | "link", string | LinkAppSvgT>;

export type SpannerAuthT = Record<AuthSpannerLinksT, AuthSpannerLinksObjT[]>;

export const authSpannerLinks: SpannerAuthT = {
  register: [
    {
      msg: "Already have an'account? Login",
      link: linkLogin,
    },
    {
      msg: "Email did not arrive? Send again",
      link: linkConfirmEmail,
    },
  ],
  login: [
    {
      msg: "Don't have an'account? Register",
      link: linkRegister,
    },
    {
      msg: "Forgot password? Recover Account",
      link: linkRecoverPwd,
    },
  ],
  "confirm-email": [
    {
      msg: "Already confirmed? Login",
      link: linkLogin,
    },
    {
      msg: "Forgot password? Recover Account",
      link: linkRecoverPwd,
    },
  ],
  "recover-password": [
    {
      msg: "Don't have an'account? Register",
      link: linkRegister,
    },
    {
      msg: "Email did not arrive? Send again",
      link: linkConfirmEmail,
    },
  ],
};
