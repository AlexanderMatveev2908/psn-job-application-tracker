import { useSelector } from "react-redux";
import { getUserState } from "../slices/slice";
import { REG_JWT } from "@/core/constants/regex";
import { useMemo } from "react";
import { isStr } from "@/core/lib/dataStructure/ect";

export const useGetUserState = () => {
  const userState = useSelector(getUserState);

  const isLogged = useMemo(
    () => REG_JWT.test(userState.access_token),
    [userState.access_token]
  );
  const canPushNonLogged = useMemo(
    () =>
      !isLogged && !userState.pendingActionSession && userState.touchedServer,
    [userState.pendingActionSession, userState.touchedServer, isLogged]
  );
  const isUsOk = useMemo(
    () => isLogged && isStr(userState.user?.id),
    [isLogged, userState.user?.id]
  );

  return {
    ...userState,
    isLogged,
    canPushNonLogged,
    isUsOk,
  };
};
