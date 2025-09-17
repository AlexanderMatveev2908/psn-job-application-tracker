import { envApp } from "@/core/constants/env";

export const wrapGetValsFormManualTest = <T>(objA: T, objB: T) =>
  !envApp.isDev ? objA : objB;

export const myPwd = "_774X^yVnpcKF1$!j(1F";
export const myMail = "matveevalexander470@gmail.com";
