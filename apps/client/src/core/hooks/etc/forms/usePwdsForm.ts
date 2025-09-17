import { useFocus } from "@/core/hooks/etc/focus/useFocus";
import { PwdsFormT, pwdsSchema, resetValsPwdsForm } from "@/core/paperwork";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";

export const usePwdsForm = () => {
  const formCtx = useForm<PwdsFormT>({
    mode: "onChange",
    resolver: zodResolver(pwdsSchema),
    defaultValues: resetValsPwdsForm,
  });

  const { setFocus } = formCtx;

  useFocus("password", { setFocus });

  return {
    formCtx,
  };
};
