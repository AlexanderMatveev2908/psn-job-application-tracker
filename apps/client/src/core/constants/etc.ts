export const genMailNoticeMsg = (txt: string) =>
  `We've sent you an email${
    txt ? ` ${txt}` : ""
  }. If you don't see it, check your spam folder, it might be partying there 🎉`;
