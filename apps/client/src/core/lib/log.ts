import { isStr } from "./dataStructure/ect";
import { formatDateDev } from "./dataStructure/formatters";

/* eslint-disable @typescript-eslint/no-explicit-any */
export const __cg = (...args: any[]) => {
  let ttl = args[0];
  const isTtlOk = isStr(ttl);
  if (!isTtlOk) ttl = "logger";

  console.group(
    `🔥 ${"—".repeat(10)}  ${ttl.toUpperCase()}  ${"—".repeat(10)} 🔥`
  );

  for (const el of isTtlOk ? args.slice(1) : args) {
    console.log(el);
  }

  console.groupEnd();

  const trace = new Error();
  const traces = trace.stack?.split("\n");

  let firstTrace: string | undefined;
  for (const t of traces ?? []) {
    if (t.includes("src") && !t.includes("lib/log.ts")) {
      firstTrace = t;
      break;
    }
  }

  const path = firstTrace?.split("src")?.[1];

  console.log(`=> ${path} 🚀 \n ${formatDateDev(Date.now())} 🕰️`);
};
