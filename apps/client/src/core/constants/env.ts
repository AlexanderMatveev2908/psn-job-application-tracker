const isDev = process.env.NEXT_PUBLIC_ENV === "development";

type EnvT = "development" | "test" | "production";

interface EnvAppT {
  isDev: boolean;
  ENV: EnvT;
  BACK_URL: string;
  NEXT_PUBLIC_SMPT_FROM: string;
}

export const envApp: EnvAppT = {
  isDev,
  ENV: process.env.NEXT_PUBLIC_ENV as EnvT,
  BACK_URL: (process.env.NEXT_PUBLIC_ENV === "test"
    ? process.env.NEXT_PUBLIC_BACK_URL_TEST
    : isDev
    ? process.env.NEXT_PUBLIC_BACK_URL_DEV
    : process.env.NEXT_PUBLIC_BACK_URL)!,
  NEXT_PUBLIC_SMPT_FROM: process.env.NEXT_PUBLIC_SMPT_FROM!,
};
