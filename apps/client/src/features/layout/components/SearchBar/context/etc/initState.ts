import { getNumCardsForPage } from "../../sideComponents/PageCounter/uiFactory";

export type SearchBarStateT = {
  bars: {
    filterBar: null | boolean;
    sortBar: null | boolean;
  };
  currFilter: string;
  pagination: {
    swap: number;
    page: number;
    limit: number;
  };

  pending: {
    submit: boolean;
    reset: boolean;
  };

  api: {
    skipCall: boolean;
  };
};

export const searchBarInitState: SearchBarStateT = {
  bars: {
    filterBar: false,
    sortBar: null,
  },
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  currFilter: "" as any,
  pagination: {
    swap: 0,
    page: 0,
    limit: getNumCardsForPage(),
  },
  pending: {
    reset: false,
    submit: false,
  },

  api: {
    skipCall: false,
  },
};
