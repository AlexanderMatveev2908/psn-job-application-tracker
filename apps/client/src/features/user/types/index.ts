import { SqlTableRoot } from "@/common/types/db";

export interface BackupCodeT extends SqlTableRoot {
  code: string;
  userId: string;
}

export interface UserT extends SqlTableRoot {
  firstName: string;
  lastName: string;
  email: string;
  terms: boolean;
  use2FA: boolean;
  isVerified: boolean;

  password?: string;
  totpSecret?: boolean;
}
