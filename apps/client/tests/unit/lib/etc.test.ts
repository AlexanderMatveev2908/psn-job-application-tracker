import { calcIsCurrPath } from "@/core/lib/path";
import { describe } from "node:test";
import { expect, it } from "vitest";

describe("test calcIsCurrPath to use it as replacement for React 'NavLink'", () => {
  it("0. should match simple equality", () => {
    expect(calcIsCurrPath("/abc", "/abc")).toBe(true);
  });

  it("1. should match with a trailing slash in path & query in href", () => {
    expect(calcIsCurrPath("/abc/", "/abc?x=123")).toBe(true);
  });

  it("2. should match with query in path & trailing slash in href", () => {
    expect(calcIsCurrPath("/abc?x=123", "/abc//")).toBe(true);
  });

  it("3. should fail simple inequality", () => {
    expect(calcIsCurrPath("/abc", "/zxy")).toBe(false);
  });

  it("4. should fail sub paths", () => {
    expect(calcIsCurrPath("/abc", "/abc/subpath")).toBe(false);
  });
});
