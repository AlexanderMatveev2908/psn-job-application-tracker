import * as React from "react";
import type { SVGProps } from "react";
const SvgPasswordCursor = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    aria-hidden="true"
    stroke="currentColor"
    {...props}
  >
    <g strokeWidth={1.5}>
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M21 13V8a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h7"
      />
      <path
        d="M20.879 16.917c.494.304.463 1.043-.045 1.101l-2.567.291l-1.151 2.312c-.228.459-.933.234-1.05-.334l-1.255-6.116c-.099-.48.333-.782.75-.525z"
        clipRule="evenodd"
      />
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="m12 11.01l.01-.011m3.99.011l.01-.011M8 11.01l.01-.011"
      />
    </g>
  </svg>
);
export default SvgPasswordCursor;
