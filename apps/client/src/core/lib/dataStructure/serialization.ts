import { ErrApp } from "../err";

/* eslint-disable @typescript-eslint/no-explicit-any */
export const isSerializable = (
  v: any,
  seen: WeakSet<any> = new WeakSet()
): boolean => {
  if (
    v === null ||
    typeof v === "string" ||
    typeof v === "number" ||
    typeof v === "boolean"
  )
    return true;

  if (typeof v === "bigint" || typeof v === "function" || typeof v === "symbol")
    return false;

  if (v instanceof Date) return true;
  if (v instanceof FormData) return false;
  if (v instanceof Map || v instanceof Set) return false;

  if (typeof v === "object") {
    if (seen.has(v)) return false;
    seen.add(v);

    return Object.values(v).every((v) => isSerializable(v, seen));
  }

  return false;
};

type JSONValT =
  | string
  | number
  | boolean
  | null
  | JSONValT[]
  | { [key: string]: JSONValT };

export const serialize = (
  data: unknown,
  seen: WeakSet<any> = new WeakSet()
): JSONValT => {
  if (
    data === null ||
    typeof data === "string" ||
    typeof data === "number" ||
    typeof data === "boolean"
  )
    return data;

  if (typeof data === "bigint") return data + "";
  if (
    typeof data === "function" ||
    typeof data === "symbol" ||
    typeof data === "undefined"
  )
    return `=> ${typeof data}`;

  if (data instanceof Date) return data.toISOString();

  if (Array.isArray(data)) {
    if (seen.has(data)) throw new ErrApp("circular reference detected");
    seen.add(data);
    return data.map((item) => serialize(item, seen));
  }

  if (data instanceof Map) {
    if (seen.has(data)) throw new ErrApp("circular reference detected");
    seen.add(data);

    const obj: Record<string, JSONValT> = {};

    for (const [k, vv] of data.entries()) {
      obj[JSON.stringify(serialize(k, seen))] = serialize(vv, seen);
    }
    return obj;
  }

  if (data instanceof Set) {
    if (seen.has(data)) throw new ErrApp("circular reference detected");
    seen.add(data);
    return Array.from(data).map((item) => serialize(item, seen));
  }

  if (data instanceof FormData) {
    if (seen.has(data)) throw new ErrApp("circular reference detected");
    seen.add(data);

    const obj: Record<string, JSONValT> = {};

    for (const [k, v] of data.entries()) {
      obj[k] = serialize(v);
    }

    return obj;
  }

  if (typeof data === "object") {
    if (seen.has(data)) throw new ErrApp("circular reference detected");
    seen.add(data);
    const obj: Record<string, JSONValT> = {};
    for (const [k, vv] of Object.entries(data)) {
      obj[k] = serialize(vv, seen);
    }
    return obj;
  }

  return null;
};
