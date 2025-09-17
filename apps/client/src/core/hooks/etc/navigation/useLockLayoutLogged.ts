import { useGetUserState } from "@/features/user/hooks/useGetUserState";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export const useLockLayoutLogged = () => {
  const { canPushNonLogged } = useGetUserState();
  const nav = useRouter();

  useEffect(() => {
    if (canPushNonLogged) nav.replace("/auth/login");
  }, [canPushNonLogged, nav]);
};
