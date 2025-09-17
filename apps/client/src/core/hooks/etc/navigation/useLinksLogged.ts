import { linksLoggedAccount } from "@/core/uiFactory/links";
import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { useMemo } from "react";

export const useLinksLogged = () => {
  const { user } = useGetUserState();

  const linksLoggedFiltered = useMemo(
    () =>
      linksLoggedAccount.filter((lk) =>
        user?.is_verified ? lk.label.toLowerCase() !== "confirm email" : lk
      ),
    [user]
  );

  return {
    linksLoggedFiltered,
  };
};
