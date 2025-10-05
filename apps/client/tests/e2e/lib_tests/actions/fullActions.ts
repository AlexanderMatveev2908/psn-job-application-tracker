import { Browser, expect, Page } from "@playwright/test";
import { genRegisterPayload, PayloadRegisterT } from "../conf/payloads";
import { preTest } from "../conf/pre";
import { REG_CBC_HMAC } from "@/core/constants/regex";
import { TokenT } from "@/common/types/tokens";
import { UserT } from "@/features/user/types";
import { BASE_URL } from "../conf/constants";

export interface GetTokensReturnT {
  accessToken: string;
  user: UserT;
  cbcHmacToken: string;
  plainPwd: string;
  page: Page;
}

export const getTokensLib = async (
  browser: Browser,
  {
    tokenType = TokenT.CONF_EMAIL,
    verifyUser = false,
    payload = genRegisterPayload(),
  }: { tokenType?: TokenT; verifyUser?: boolean; payload?: PayloadRegisterT }
): Promise<GetTokensReturnT> => {
  const page = await preTest(browser, "/");

  const res = await page.request.post(
    `${BASE_URL}/test/user?tokenT=${tokenType}&verifyUser=${verifyUser}`,
    {
      data: {
        existingPayload: payload,
      },
    }
  );
  const data = await res.json();

  await page.evaluate((token: string) => {
    sessionStorage.setItem("accessToken", token);
  }, data.accessToken);

  expect(REG_CBC_HMAC.test(data.cbcHmacToken));

  return { ...data, plainPwd: data?.plainPwd ?? payload.password, page };
};

export interface ResUser2FA {
  payload: PayloadRegisterT;
  user: UserT;
  totp_secret: string;
  backup_codes: string[];
  accessToken: string;
  cbcHmacToken: string;
}

export const getUser2FA = async (
  browser: Browser,
  {
    tokenType = TokenT.CONF_EMAIL,
  }: {
    tokenType?: TokenT;
  }
): Promise<ResUser2FA & { page: Page }> => {
  const page = await preTest(browser, "/");

  const res = await page.request.post(
    `${BASE_URL}/test/get-user-2FA?cbc_hmac_t=${tokenType}`
  );
  const data = await res.json();

  await page.evaluate((token: string) => {
    sessionStorage.setItem("accessToken", token);
  }, data.accessToken);

  return { ...data, page: page };
};
