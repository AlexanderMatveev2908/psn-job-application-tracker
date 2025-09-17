/** @jsxImportSource @emotion/react */
"use client";

import { ChildrenT } from "@/common/types/ui";
import { REG_CBC_HMAC, REG_JWT } from "@/core/constants/regex";
import { useWrapQuery } from "@/core/hooks/api/useWrapQuery";
import { useDelCbcHmacByPathAndType } from "@/features/user/hooks/useDelCbcHmacByPathAndType";
import { useWrapClientListener } from "@/core/hooks/etc/hydration/useWrapClientListener";
import { useScroll } from "@/core/hooks/etc/navigation/useScroll";
import { getStorage } from "@/core/lib/storage";
import { noticeSlice } from "@/features/notice/slices/slice";
import { useEndPendingActionUser } from "@/features/user/hooks/useEndPendingActionUser";
import { userSliceAPI } from "@/features/user/slices/api";
import { userSlice } from "@/features/user/slices/slice";
import { useEffect, type FC } from "react";
import { useDispatch } from "react-redux";
import { isObjOk } from "@/core/lib/dataStructure/ect";

const CallbacksWrapper: FC<ChildrenT> = ({ children }) => {
  // ? ui
  useScroll();

  // ? user profile
  useWrapQuery({
    ...userSliceAPI.useGetProfileQuery(),
  });

  // ? auth mngmnt
  useEndPendingActionUser();
  useDelCbcHmacByPathAndType();

  const dispatch = useDispatch();
  const { wrapClientListener } = useWrapClientListener();

  // ? storage stuff
  useEffect(() => {
    const cb = () => {
      const access_token = (getStorage("access_token") ?? "") as string;
      const notice = getStorage("notice");
      const cbc_hmac_token = (getStorage("cbc_hmac_token") ?? "") as string;

      if (REG_JWT.test(access_token))
        dispatch(userSlice.actions.setAccessToken({ access_token }));
      if (REG_CBC_HMAC.test(cbc_hmac_token))
        dispatch(userSlice.actions.setCbcHmac(cbc_hmac_token));
      if (isObjOk(notice)) dispatch(noticeSlice.actions.setNotice(notice!));
    };

    wrapClientListener(cb);
  }, [wrapClientListener, dispatch]);

  return children;
};

export default CallbacksWrapper;
