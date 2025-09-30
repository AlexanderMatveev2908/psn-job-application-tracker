import { REG_INT } from "@/core/constants/regex";
import { ToptFormT } from "@/core/paperwork";
import { useEffect, useState } from "react";
import {
  UseFormSetFocus,
  UseFormSetValue,
  UseFormTrigger,
} from "react-hook-form";

type Params = {
  setValue: UseFormSetValue<ToptFormT>;
  setFocus: UseFormSetFocus<ToptFormT>;
  trigger: UseFormTrigger<ToptFormT>;
  totpCode: string[];
  isCurr: boolean;
};

export const useSideStuffTotpForm = ({
  setValue,
  setFocus,
  trigger,
  totpCode,
  isCurr,
}: Params) => {
  const [ctrlPressed, setCtrlPressed] = useState<boolean>();
  const [currFocus, setCurrFocus] = useState<number | null>(null);

  useEffect(() => {
    const handlePaste = (e: ClipboardEvent) => {
      if (!isCurr) return;
      e.preventDefault();

      const pasted = e.clipboardData?.getData("text") ?? "";
      const parsed = pasted.split("").slice(0, 6);

      setValue("totpCode", parsed, { shouldValidate: true });
    };

    window.addEventListener("paste", handlePaste);

    return () => {
      window.removeEventListener("paste", handlePaste);
    };
  }, [setValue, isCurr]);

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (!isCurr) return;

      const defVals = Array(6).fill("");

      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "a") {
        setCtrlPressed(true);
        return;
      }

      if (ctrlPressed) {
        const possibleVal = e.key.trim();

        if (e.key === "Backspace" || !possibleVal) {
          setValue("totpCode", defVals, { shouldValidate: true });
        } else {
          setValue("totpCode", [possibleVal, ...Array(5).fill("")], {
            shouldValidate: true,
          });
        }

        setCtrlPressed(false);
        setFocus("totpCode.0");
        trigger();
        return;
      }

      if (e.key === "Backspace" && typeof currFocus === "number") {
        setValue(
          "totpCode",
          totpCode.map((v, idx) => (idx === currFocus ? "" : v)),
          { shouldValidate: true }
        );
        setFocus(`totpCode.${currFocus - 1 >= 0 ? currFocus - 1 : 0}`);
        trigger();
        return;
      }

      if (REG_INT.test(e.key) && typeof currFocus === "number") {
        setValue(
          "totpCode",
          totpCode.map((v, idx) => (idx === currFocus ? e.key : v)),
          { shouldValidate: true }
        );
        setFocus(`totpCode.${Math.min(currFocus + 1, 5)}`);
        trigger();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [ctrlPressed, currFocus, totpCode, setValue, setFocus, trigger, isCurr]);

  return {
    ctrlPressed,
    setCtrlPressed,
    setCurrFocus,
  };
};
