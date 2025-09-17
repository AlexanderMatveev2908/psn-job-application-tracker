import { createContext } from "react";
import { SearchBarCtxT } from "./hooks/useSearchCtxProvider";

export const SearchBarCtx = createContext<SearchBarCtxT | null>(null);
