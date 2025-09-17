import { ErrApp } from "@/core/lib/err";
import { SearchBarStateT } from "./initState";
import { SearchBarReducerActionsT } from "./actions";

export const searchbarReducer = (
  state: SearchBarStateT,
  action: SearchBarReducerActionsT
): SearchBarStateT => {
  switch (action.type) {
    case "SET_BAR": {
      const { bar, val } = action.payload;

      return {
        ...state,
        bars: {
          ...state.bars,
          [bar]: val,
        },
      };
    }

    case "SET_CURR_FILTER":
      return {
        ...state,
        currFilter: action.payload.val,
      };

    case "SET_PAGINATION": {
      const { key, val } = action.payload;

      return {
        ...state,
        pagination: {
          ...state.pagination,
          [key]: val,
        },
      };
    }

    case "SET_PENDING":
      const { key, val } = action.payload;

      return {
        ...state,
        pending: {
          ...state.pending,
          [key]: val,
        },
      };

    case "SET_API": {
      const { key, val } = action.payload;

      return {
        ...state,
        api: {
          ...state.api,
          [key]: val,
        },
      };
    }

    default:
      throw new ErrApp(`Invalid action 😡 => ${action}`);
  }
};
