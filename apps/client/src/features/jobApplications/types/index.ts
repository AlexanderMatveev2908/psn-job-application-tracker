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
  companyName: string;
  userId: string;
  positionName: string;
  status: ApplicationStatusT;
  appliedAt: number;
  notes?: string;
}
