import { Browser, expect } from "@playwright/test";
import {
  BASE_URL,
  genPayloadJobAppl,
  getByID,
  getTokensLib,
} from "../../lib_tests/idx";
import { REG_ID } from "@/core/constants/regex";
import { JobApplicationT } from "@/features/jobApplications/types";

export const preJobApplRead = async (browser: Browser) => {
  const { page, access_token, ...rst } = await getTokensLib(browser, {});

  const applications: JobApplicationT[] = [];

  for (let i = 0; i < 5; i++) {
    const payload = genPayloadJobAppl();

    const res = await page.request.post(`${BASE_URL}/job-applications`, {
      data: payload,
      headers: {
        authorization: `Bearer ${access_token}`,
      },
    });

    const { job_application } = await res.json();

    await expect(REG_ID.test(job_application.id)).toBeTruthy();

    applications.push(job_application);
  }

  await expect(applications.length).toBe(5);

  await page.goto("/job-applications/read");

  const searchBar = await getByID(page, "search_bar");
  const spanHits = await getByID(searchBar, "search_bar__n_hits");

  await expect(await spanHits.innerText()).toBe("5");

  return {
    ...rst,
    page,
    access_token,
    searchBar,
    applications,
    spanHits,
  };
};
