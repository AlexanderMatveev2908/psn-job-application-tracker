import * as React from "react";
import type { SVGProps } from "react";
const SvgBurger = (props: SVGProps<SVGSVGElement>) => (
  <svg
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
    aria-hidden="true"
    stroke="currentColor"
    {...props}
  >
    <path d="M4 18L20 18" strokeWidth={2} strokeLinecap="round" />
    <path d="M4 12L20 12" strokeWidth={2} strokeLinecap="round" />
    <path d="M4 6L20 6" strokeWidth={2} strokeLinecap="round" />
  </svg>
);
export default SvgBurger;
