import * as React from "react";
import type { SVGProps } from "react";
const SvgLogo = (props: SVGProps<SVGSVGElement>) => (
  <svg
    viewBox="0 0 24 24"
    id="job"
    data-name="Flat Line"
    xmlns="http://www.w3.org/2000/svg"
    className="icon flat-line"
    aria-hidden="true"
    stroke="currentColor"
    {...props}
  >
    <g id="SVGRepo_iconCarrier">
      <rect
        id="secondary"
        x={5}
        y={5}
        width={14}
        height={18}
        rx={1}
        transform="translate(26 2) rotate(90)"
        style={{
          fill: "#0a0a0a",
          strokeWidth: 2,
        }}
      />
      <path
        id="primary"
        d="M16,7H8V4A1,1,0,0,1,9,3h6a1,1,0,0,1,1,1Zm1,4H7m8,0v2m6,7V8a1,1,0,0,0-1-1H4A1,1,0,0,0,3,8V20a1,1,0,0,0,1,1H20A1,1,0,0,0,21,20Z"
        style={{
          fill: "none",
          stroke: "#f5f5f5",
          strokeLinecap: "round",
          strokeLinejoin: "round",
          strokeWidth: 2,
        }}
      />
    </g>
  </svg>
);
export default SvgLogo;
