/* eslint-disable @typescript-eslint/no-explicit-any */
export const isStr = (str: unknown): boolean =>
  typeof str === "string" && !!str.trim().length;

export const isObjOk = (obj: any, cb?: (v: any) => boolean): boolean =>
  typeof obj === "object" &&
  obj !== null &&
  !!Object.keys(obj).length &&
  Object.values(obj).some((v) => (typeof cb === "function" ? cb(v) : true));

export const isArrOk = (arr: any, cb?: (v: any) => boolean) =>
  Array.isArray(arr) &&
  arr.length &&
  arr.every((el) => (typeof cb === "function" ? cb(el) : true));

export const cpyObj = <T>(obj: T): T => {
  if (obj === null || typeof obj !== "object") return obj;

  if (typeof obj === "function") return obj as T;

  if (obj instanceof Date) return new Date(obj.getTime()) as T;

  if (obj instanceof RegExp) return new RegExp(obj.source, obj.flags) as T;

  if (obj instanceof Set)
    return new Set(Array.from(obj, (v) => cpyObj(v))) as T;

  if (obj instanceof Map)
    return new Map(
      Array.from(obj.entries(), ([k, v]) => [cpyObj(k), cpyObj(v)])
    ) as T;

  if (Array.isArray(obj)) return obj.map((v) => cpyObj(v)) as T;

  const cpy: Record<string, unknown> = {};
  for (const k in obj)
    if (Object.prototype.hasOwnProperty.call(obj, k)) cpy[k] = cpyObj(obj[k]);

  return cpy as T;
};

type KeyT = string | number | symbol;

export const isSameObj = (
  a: unknown,
  b: unknown,
  seen = new WeakMap()
): boolean => {
  if (a === b) return true;

  if (typeof a === "number" && typeof b === "number" && isNaN(a) && isNaN(b))
    return true;

  if (
    a === null ||
    b === null ||
    typeof a !== "object" ||
    typeof b !== "object"
  )
    return false;

  if (seen.get(a) === b) return true;
  seen.set(a, b);

  if (a instanceof Date && b instanceof Date)
    return a.getTime() === b.getTime();

  if (a instanceof RegExp && b instanceof RegExp)
    return a.source === b.source && a.flags === b.flags;

  if (Array.isArray(a) && Array.isArray(b)) {
    if (a.length !== b.length) return false;
    return a.every((val, i) => isSameObj(val, b[i], seen));
  }

  if (a instanceof Set && b instanceof Set) {
    if (a.size !== b.size) return false;
    for (const val of a) {
      if (![...b].some((v) => isSameObj(val, v, seen))) return false;
    }

    return true;
  }

  if (a instanceof Map && b instanceof Map) {
    if (a.size !== b.size) return false;
    for (const [k, v] of a) {
      if (!b.has(k) || !isSameObj(v, b.get(k), seen)) return false;
    }

    return true;
  }

  const keysA = Reflect.ownKeys(a);
  const keysB = Reflect.ownKeys(b);
  if (keysA.length !== keysB.length) return false;

  return keysA.every((k) =>
    isSameObj((a as Record<KeyT, any>)[k], (b as Record<KeyT, any>)[k], seen)
  );
};
