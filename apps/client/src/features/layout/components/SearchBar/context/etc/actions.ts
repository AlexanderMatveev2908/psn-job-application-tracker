export type PayloadSetBarT = {
  bar: "filterBar" | "sortBar";
  val: boolean | null;
};

export type PayloadPaginationT = {
  key: "page" | "swap" | "limit";
  val: number;
};

export type PayloadPendingT = {
  key: "submit" | "reset";
  val: boolean;
};

export type PayloadSearchAPIT = {
  key: "skipCall";
  val: boolean;
};

export type SearchBarReducerActionsT =
  | {
      type: "SET_BAR";
      payload: PayloadSetBarT;
    }
  | {
      type: "SET_CURR_FILTER";
      payload: { val: string };
    }
  | {
      type: "SET_PAGINATION";
      payload: PayloadPaginationT;
    }
  | {
      type: "SET_PENDING";
      payload: PayloadPendingT;
    }
  | {
      type: "SET_API";
      payload: PayloadSearchAPIT;
    };
