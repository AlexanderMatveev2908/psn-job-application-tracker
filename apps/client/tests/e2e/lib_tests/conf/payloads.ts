import { genLorem, genMinMax, pickRandom } from "@/core/lib/etc";
import { genPwd } from "@/core/lib/pwd";
import { ApplicationStatusT } from "@/features/jobApplications/types";
import { faker } from "@faker-js/faker";

export interface PayloadRegisterT {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  terms: boolean;
}

export const genRegisterPayload = (): PayloadRegisterT => {
  const pwd = genPwd();

  return {
    firstName: faker.person.firstName(),
    lastName: faker.person.lastName(),
    email: faker.internet.email(),
    password: pwd,
    confirmPassword: pwd,
    terms: true,
  };
};

export interface PayloadJobApplT {
  companyName: string;
  positionName: string;
  status: ApplicationStatusT;
  appliedAt: string;
  notes?: string;
}

const MS_PER_DAY = 1000 * 60 ** 2 * 24;

const getRandomAppliedAtDate = () => {
  const sixMonthsMs = MS_PER_DAY * 30 * 6;
  const randomOffset = genMinMax(0, sixMonthsMs);

  return new Date(Date.now() - randomOffset).toISOString().split("T")[0];
};

export const genPayloadJobAppl = (): PayloadJobApplT => ({
  companyName: faker.company.name(),
  positionName: faker.person.jobTitle(),
  appliedAt: getRandomAppliedAtDate(),
  status: pickRandom(Object.values(ApplicationStatusT)) as ApplicationStatusT,
  notes: genLorem(4),
});
