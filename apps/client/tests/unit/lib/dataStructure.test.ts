import { capt } from "@/core/lib/dataStructure/formatters";
import { describe, expect, it } from "vitest";

describe("fn", () => {
  it("capt str", () => {
    expect(capt("abc")).toBe("Abc");
  });
});
