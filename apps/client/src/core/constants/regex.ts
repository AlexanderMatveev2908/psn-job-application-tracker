export const REG_NAME = /^[\p{L}\s,`'\-]*$/u;
export const REG_JOB_NAME = /^[\p{L}\s,`'\/\-]*$/u;
export const REG_PWD = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])\S{8,}$/u;
export const REG_TXT = /^[\p{L}\d\s\-'"\.,;!?]*$/u;

export const REG_INT = /^\d+$/;
export const REG_FLOAT = /^(?:\d+(?:\.\d{1,2})?|\.\d{1,2})$/;

export const REG_ID =
  /^([a-f0-9]{8})-([a-f0-9]{4})-4[a-f0-9]{3}-([a-f0-9]{4})-([a-f0-9]{12})$/;

export const REG_JWT =
  /^(?=.{171}$)[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+$/;
export const REG_JWE = /^[A-Fa-f0-9]{1004}$/;
export const REG_CBC_HMAC =
  /^(?=.{600,}$)[A-Fa-f0-9]{400,}\.[A-Fa-f0-9]{32}\.[A-Fa-f0-9]{128}\.[A-Fa-f0-9]{64}$/;

export const REG_TOTP_SECRET = /^[A-Z2-7]{32}$/;
export const REG_TOTP_CODE = /^\d{6}$/;
export const REG_BACKUP_CODE = /^[A-F0-9]{4}-[A-F0-9]{4}$/;

export const REG_DATE_PICKER =
  /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/;
