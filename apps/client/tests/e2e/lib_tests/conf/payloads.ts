import { genLorem, genMinMax, pickRandom } from "@/core/lib/etc";
import { genPwd } from "@/core/lib/pwd";
import { ApplicationStatusT } from "@/features/jobApplications/types";
import { faker } from "@faker-js/faker";

export interface PayloadRegisterT {
  first_name: string;
  last_name: string;
  email: string;
  password: string;
  confirm_password: string;
  terms: boolean;
}

export const genRegisterPayload = (): PayloadRegisterT => {
  const pwd = genPwd();

  return {
    first_name: faker.person.firstName(),
    last_name: faker.person.lastName(),
    email: faker.internet.email(),
    password: pwd,
    confirm_password: pwd,
    terms: true,
  };
};

export interface PayloadJobApplT {
  company_name: string;
  position_name: string;
  status: ApplicationStatusT;
  applied_at: string;
  notes?: string;
}

const MS_PER_DAY = 1000 * 60 ** 2 * 24;

const getRandomAppliedAtDate = () => {
  const sixMonthsMs = MS_PER_DAY * 30 * 6;
  const randomOffset = genMinMax(0, sixMonthsMs);

  return new Date(Date.now() - randomOffset).toISOString().split("T")[0];
};

export const genPayloadJobAppl = (): PayloadJobApplT => ({
  company_name: faker.company.name(),
  position_name: faker.person.jobTitle(),
  applied_at: getRandomAppliedAtDate(),
  status: pickRandom(Object.values(ApplicationStatusT)) as ApplicationStatusT,
  notes: genLorem(4),
});
