"use client";

import styled from "@emotion/styled";

type PropsType = {
  $clr: string;
};

export const BtnIconStyled = styled.button<PropsType>`
  color: ${({ $clr }) => $clr};

  .btn_icons__content > span {
    color: var(--white__0);
  }

  .btn_icons__content,
  .btn_icons__shadow {
    padding: 0.5rem 1rem;
    transition: 0.3s;
    border-radius: 1rem;
    background: var(--neutral__950);
    overflow: hidden;

    &::after {
      content: "";
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
      border-radius: 1rem;
      background: transparent;
      border: 3px solid ${({ $clr }) => $clr};
      z-index: 10;
    }
  }

  &:enabled:hover {
    rotate: -5deg;
    transform: scale(1.1);

    .btn_icons__shadow {
      transform: translate(-4%, 15%);
    }

    .btn_icons__ref_0 {
      transform: translateX(2000%) skew(-35deg);
    }
    .btn_icons__ref_1 {
      transition-delay: 0.1s;
      transform: translateX(750%) skew(-35deg);
    }
  }
  &:enabled:active {
    rotate: 0deg;
    transform: scale(0.8);

    .btn_icons__shadow {
      transform: translate(0, 0);
    }

    .btn_icons__ref_0 {
      transform: translateX(0) skew(0);
    }
    .btn_icons__ref_1 {
      transform: translateX(0) skew(0);
    }
  }

  .btn_icons__ref_0,
  .btn_icons__ref_1 {
    position: absolute;
    min-height: 200%;
    top: -50%;
    background: linear-gradient(
      to right,
      transparent 0%,
      var(--neutral__400) 50%,
      transparent 100%
    );
    transform: skewX(-35deg);
    width: 10%;

    &::after {
      content: "";
      position: absolute;
      top: 0;
      height: 100%;
      left: -250%;
      width: 150%;
      background: linear-gradient(
        to right,
        transparent 0%,
        var(--neutral__500) 50%,
        transparent 100%
      );
      filter: blur(2.5px);
      mix-blend-mode: overlay;
      opacity: 0.9;
    }
  }
  .btn_icons__ref_0 {
    left: -50%;
    transition: 0.4s;
  }
  .btn_icons__ref_1 {
    left: -50%;
    transition: 0.3s;
  }
`;
