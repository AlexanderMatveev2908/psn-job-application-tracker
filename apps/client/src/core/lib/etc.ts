import { RefObject } from "react";

export const clearTmr = (timerID: RefObject<NodeJS.Timeout | null>) => {
  if (!timerID.current) return;

  clearTimeout(timerID.current);
  timerID.current = null;
};

export const isWdw = () => typeof window !== "undefined";

export const genLorem = (n?: number) =>
  "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Exercitationem perferendis nostrum, sapiente dicta praesentium neque ratione rem facilis. Alias quos libero vel iusto quam in, recusandae accusamus cupiditate fugiat nam.".repeat(
    n ?? 1
  );

export const genMinMax = (min: number, max: number) =>
  Math.floor(Math.random() * (max - min + 1) + min);

export const pickRandom = (arr: string[]) => arr[genMinMax(0, arr.length - 1)];

export const parseLabelToTestID = (label: string) =>
  label.toLowerCase().replaceAll(" ", "_");

export const skipUselessFalsy = (v: unknown) =>
  !v && typeof v !== "number" && typeof v !== "boolean";
