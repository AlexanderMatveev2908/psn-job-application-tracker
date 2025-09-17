import { StorageKey } from "@/common/types/storage";
import { isWdw } from "./etc";

export const saveStorage = <T>(data: T, { key }: { key: StorageKey }): void =>
  sessionStorage.setItem(
    key,
    typeof data === "string" ? data : JSON.stringify(data)
  );

export const getStorage = <T>(key: StorageKey): T | null => {
  if (!isWdw()) return null;

  const data = sessionStorage.getItem(key);

  if (!data || data === "null") return null;

  try {
    return JSON.parse(data);
  } catch {
    return data as T;
  }
};

export const delStorageItm = (key: StorageKey) =>
  sessionStorage.removeItem(key);

export const clearStorage = () => sessionStorage.clear();
