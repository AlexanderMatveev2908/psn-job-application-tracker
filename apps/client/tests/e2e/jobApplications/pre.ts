/* eslint-disable @typescript-eslint/no-explicit-any */
import { Browser, expect } from "@playwright/test";
import { BASE_URL, genPayloadJobAppl, getTokensLib } from "../lib_tests/idx";
import { JobApplicationT } from "@/features/jobApplications/types";

export const relevantKeysJobAppl = ["company_name", "position_name", "status"];

export const preJobAppl = async (browser: Browser) => {
  const { page, access_token, ...rst } = await getTokensLib(browser, {});

  const originalPayload = genPayloadJobAppl();

  const resPost = await page.request.post(`${BASE_URL}/job-applications`, {
    data: originalPayload,
    headers: {
      authorization: `Bearer ${access_token}`,
    },
  });

  const { job_application } = (await resPost.json()) as {
    job_application: JobApplicationT;
  };

  for (const k of relevantKeysJobAppl)
    await expect(
      (job_application as any)[k] === (originalPayload as any)[k]
    ).toBe(true);

  return {
    ...rst,
    page,
    access_token,
    job_application,
    originalPayload,
  };
};
