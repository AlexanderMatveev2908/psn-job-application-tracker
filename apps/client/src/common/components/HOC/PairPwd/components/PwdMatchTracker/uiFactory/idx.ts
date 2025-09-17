import SvgLowercase from "@/common/components/SVGs/Lowercase";
import SvgNumbers from "@/common/components/SVGs/Numbers";
import SvgRuler from "@/common/components/SVGs/Ruler";
import SvgSymbols from "@/common/components/SVGs/Symbols";
import SvgUppercase from "@/common/components/SVGs/Uppercase";

export const rulesPwd = [
  { Svg: SvgLowercase, reg: /[a-z]/ },
  { Svg: SvgUppercase, reg: /[A-Z]/ },
  { Svg: SvgNumbers, reg: /\d/ },
  { Svg: SvgSymbols, reg: /[\W_]/ },
];

export const lengthPwd = { Svg: SvgRuler, reg: /^\S{8,}$/ };

export const extractClr = (reg: RegExp, pwd: string) =>
  reg.test(pwd) ? "text-green-600" : "text-red-600";
