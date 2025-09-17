/* eslint-disable @typescript-eslint/no-explicit-any */
import "@testing-library/jest-dom";
import { vi } from "vitest";
import React from "react";

vi.mock("next/navigation", () => ({
  useRouter: () => ({ push: vi.fn(), replace: vi.fn(), back: vi.fn() }),
  usePathname: () => "/",
  useSearchParams: () => new URLSearchParams(),
}));

vi.mock("next/link", () => ({
  __esModule: true,
  default: ({ href, children, ...props }: any) =>
    React.createElement("a", { href, ...props }, children),
}));

vi.mock("next/image", () => ({
  __esModule: true,
  default: (props: any) => React.createElement("img", { ...props }),
}));
