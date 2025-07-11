"use client"

import { createContext, useContext } from "react";

type SearchContextType = {
  handleSearch: (keyword: string) => void;
};

export const SearchContext = createContext<SearchContextType>({
  handleSearch: () => {},
});

export const useSearch = () => useContext(SearchContext);
