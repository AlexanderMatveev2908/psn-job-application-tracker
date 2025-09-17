import { SqlTableRoot } from "@/common/types/db";

export enum ApplicationStatusT {
  APPLIED = "APPLIED",
  UNDER_REVIEW = "UNDER_REVIEW",
  INTERVIEW = "INTERVIEW",
  OFFER = "OFFER",
  REJECTED = "REJECTED",
  WITHDRAWN = "WITHDRAWN",
}

export interface JobApplicationT extends SqlTableRoot {
  company_name: string;
  user_id: string;
  position_name: string;
  status: ApplicationStatusT;
  applied_at: number;
  notes?: string;
}
