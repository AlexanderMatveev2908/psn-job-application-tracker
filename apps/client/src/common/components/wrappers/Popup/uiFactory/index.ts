import { easeInOut } from "framer-motion";

const scaleOpenX = [0, 0.6, 1.3, 0.8, 1.1, 1];
const scaleOpenY = [0, 1.4, 0.7, 1.2, 0.9, 1];

export const varPop = {
  open: {
    scaleX: scaleOpenX,
    scaleY: scaleOpenY,
    transition: {
      duration: 0.8,
      ease: easeInOut,
    },
  },
  close: {
    scaleX: 1,
    scaleY: 0,
    transition: {
      duration: 0.25,
      ease: easeInOut,
    },
  },
};
