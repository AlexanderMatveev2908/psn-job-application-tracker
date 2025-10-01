import { genPwd } from "@/core/lib/pwd";
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
