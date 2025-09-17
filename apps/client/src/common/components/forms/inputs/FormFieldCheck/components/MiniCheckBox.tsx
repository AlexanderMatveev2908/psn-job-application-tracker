/** @jsxImportSource @emotion/react */
"use client";

import type { FC } from "react";
import { easeInOut, motion } from "framer-motion";

type PropsType = {
  isChecked: boolean;
};

const MiniCheckBox: FC<PropsType> = ({ isChecked }) => {
  return (
    <>
      <motion.div
        key={isChecked + ""}
        initial={{
          scaleX: 1,
          scaleY: 1,
        }}
        transition={{
          duration: 0.75,
          ease: easeInOut,
        }}
        animate={{
          scaleX: [0.6, 1.4, 0.8, 0.9, 1],
          scaleY: [1.4, 0.6, 1.2, 1.1, 1],
        }}
        className="border-[3px] absolute rounded-xl inset-0 border-neutral-600"
      ></motion.div>

      <motion.div
        className="absolute -top-[10px] -left-[5px] w-full h-[70%] border-[5px] border-green-600 border-t-transparent border-l-transparent"
        initial={{
          scale: 0,
          rotate: 22.5,
        }}
        transition={{
          duration: 0.2,
          delay: 0.1,
          ease: easeInOut,
        }}
        animate={{
          scale: isChecked ? [0, 1.4, 1] : 0,
          rotate: isChecked ? [22.5, 45] : 0,
        }}
      ></motion.div>
    </>
  );
};

export default MiniCheckBox;
