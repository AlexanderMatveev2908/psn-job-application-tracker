import { KeyCbT } from "../slices/slice";

export const mapperCbs: Record<KeyCbT, () => void> = {
  LOGIN: () => console.log("action mapped for key => LOGIN"),
};
