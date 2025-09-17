import { ResApiT } from "@/common/types/api";
import { isStr } from "@/core/lib/dataStructure/ect";
import { toastSlice } from "@/features/layout/components/Toast/slices";
import { useCallback } from "react";
import { useDispatch } from "react-redux";

type Params<T> = {
  data: ResApiT<T>["data"];
  showToast?: boolean;
};

export const useMsgAPI = () => {
  const dispatch = useDispatch();

  const handleMsgSession = useCallback(
    <T>({ data, showToast }: Params<T>) => {
      if (data?.restoredSession) {
        dispatch(
          toastSlice.actions.open({
            msg: "session restored",
            type: "OK",
          })
        );
      } else {
        if (showToast)
          dispatch(
            toastSlice.actions.open({
              msg: isStr(data?.msg) ? data!.msg! : "Things went good ✅",
              type: "OK",
            })
          );
      }
    },
    [dispatch]
  );

  return {
    handleMsgSession,
  };
};
