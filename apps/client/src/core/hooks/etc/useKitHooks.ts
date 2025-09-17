import { useDispatch } from "react-redux";
import { useWrapAPI } from "../api/useWrapAPI";
import { useRouter } from "next/navigation";
import { useNotice } from "@/features/notice/hooks/useNotice";

export const useKitHooks = () => {
  const dispatch = useDispatch();

  const nav = useRouter();

  const { wrapAPI } = useWrapAPI();
  const { setNotice } = useNotice();

  return {
    dispatch,
    nav,
    wrapAPI,
    setNotice,
  };
};
