import { AadCbcHmacT } from "@/common/types/tokens";
import { REG_CBC_HMAC } from "../../constants/regex";
import { ErrApp } from "../err";

export const hexToBytes = (hex: string) => {
  const arg = new Uint8Array(hex.length / 2);

  let i = 0;

  while (i < arg.length) {
    arg[i] = parseInt(hex.substring(i * 2, i * 2 + 2), 16);
    i++;
  }

  return arg;
};

export const hexToDict = (hex: string) =>
  JSON.parse(new TextDecoder().decode(hexToBytes(hex)));

export const extractAadFromCbcHmac = (
  cbc_hmac_token?: string | null
): AadCbcHmacT | null => {
  let aad: AadCbcHmacT | null = null;
  try {
    if (cbc_hmac_token && REG_CBC_HMAC.test(cbc_hmac_token))
      aad = hexToDict(cbc_hmac_token!.split(".")[0]!);
  } catch {
    aad = null;
  }

  return aad;
};

export const hexToRgb = (hex: string): string => {
  let clean = hex.replace("#", "").toLowerCase();

  if (clean.length === 3)
    clean = clean
      .split("")
      .map((ch) => ch + ch)
      .join("");

  const match = clean.match(/.{2}/g);
  if (!match || match.length < 3) {
    throw new ErrApp(`Invalid hex color: ${hex}`);
  }

  const [r, g, b] = match.map((x) => parseInt(x, 16));

  return `rgb(${r}, ${g}, ${b})`;
};

const ALPHB32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

export const b32ToHex = (str: string) => {
  let buffer = 0;
  let bitsLeft = 0;
  const output = [];

  for (const char of str.replace(/=+$/, "")) {
    const idx = ALPHB32.indexOf(char.toUpperCase());
    if (idx === -1) throw new ErrApp("Invalid b32 => " + char);

    buffer = (buffer << 5) | idx;
    bitsLeft += 5;

    if (bitsLeft >= 8) {
      bitsLeft -= 8;
      output.push((buffer >> bitsLeft) & 0xff);
    }
  }

  return output.map((b) => b.toString(16).padStart(2, "0")).join("");
};

const ALPHB64 =
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

export const b64ToHex = (str: string) => {
  let buffer = 0;
  let bitsLeft = 0;
  const output = [];

  for (const char of str.replace(/=+$/, "")) {
    const idx = ALPHB64.indexOf(char);
    if (idx === -1) throw new ErrApp("Invalid b64 => " + char);

    buffer = (buffer << 6) | idx;
    bitsLeft += 6;

    if (bitsLeft >= 8) {
      bitsLeft -= 8;
      output.push((buffer >> bitsLeft) & 0xff);
    }
  }
  return output.map((b) => b.toString(16).padStart(2, "0")).join("");
};
