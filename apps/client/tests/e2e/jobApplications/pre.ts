/* eslint-disable @typescript-eslint/no-explicit-any */
import { Browser, expect } from "@playwright/test";
import { BASE_URL, genPayloadJobAppl, getTokensLib } from "../lib_tests/idx";
import { JobApplicationT } from "@/features/jobApplications/types";

export const relevantKeysJobAppl = ["companyName", "positionName", "status"];

export const preJobAppl = async (browser: Browser) => {
  const { page, accessToken, ...rst } = await getTokensLib(browser, {});

  const originalPayload = genPayloadJobAppl();

  const resPost = await page.request.post(`${BASE_URL}/job-applications`, {
    data: originalPayload,
    headers: {
      authorization: `Bearer ${accessToken}`,
    },
  });

  const { jobApplication } = (await resPost.json()) as {
    jobApplication: JobApplicationT;
  };

  for (const k of relevantKeysJobAppl)
    await expect(
      (jobApplication as any)[k] === (originalPayload as any)[k]
    ).toBe(true);

  return {
    ...rst,
    page,
    accessToken,
    jobApplication,
    originalPayload,
  };
};
