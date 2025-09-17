import { SqlTableRoot } from "@/common/types/db";

export interface BackupCodeT extends SqlTableRoot {
  code: string;
  user_id: string;
}

export interface UserT extends SqlTableRoot {
  first_name: string;
  last_name: string;
  email: string;
  terms: boolean;
  use_2FA: boolean;
  is_verified: boolean;

  password?: string;
  totp_secret?: boolean;
}
