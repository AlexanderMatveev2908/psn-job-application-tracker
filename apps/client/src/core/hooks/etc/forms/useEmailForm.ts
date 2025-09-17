import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { EmailFormT, emailSchema, resetValsEmailForm } from "@/core/paperwork";
import { useFocus } from "@/core/hooks/etc/focus/useFocus";
import { useKitHooks } from "@/core/hooks/etc/useKitHooks";
import {
  RequireEmailEndpointT,
  requireEmailSliceAPI,
} from "@/features/requireEmail/slices/api";
import { genMailNoticeMsg } from "@/core/constants/etc";
import { logFormErrs } from "@/core/lib/forms";

type Params = {
  endpointT: RequireEmailEndpointT;
  msgNotice: string;
};

export const useEmailForm = () => {
  const formCtx = useForm<EmailFormT>({
    mode: "onChange",
    resolver: zodResolver(emailSchema),
    defaultValues: resetValsEmailForm,
  });

  const { setFocus, handleSubmit, reset } = formCtx;
  useFocus("email", { setFocus });

  const { wrapAPI, setNotice, nav } = useKitHooks();
  const [mutate, { isLoading }] =
    requireEmailSliceAPI.useRequireEmailMutation();

  const handleSaveMaker = (args: Params) =>
    handleSubmit(async (data) => {
      const res = await wrapAPI({
        cbAPI: () => mutate({ ...data, endpoint: args.endpointT }),
      });

      if (!res) return;

      reset(resetValsEmailForm);

      setNotice({
        msg: genMailNoticeMsg(args.msgNotice),
        type: "OK",
        child: "OPEN_MAIL_APP",
      });

      nav.replace("/notice");
    }, logFormErrs);

  return {
    formCtx,
    isLoading,
    handleSaveMaker,
  };
};
