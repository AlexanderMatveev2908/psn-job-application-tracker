export const genASCI = () => {
  const upper = Array.from({ length: 26 }, (_, i) =>
    String.fromCharCode(65 + i)
  );
  const lower = Array.from({ length: 26 }, (_, i) =>
    String.fromCharCode(97 + i)
  );
  const nums = Array.from({ length: 10 }, (_, i) => i);

  const ranges: [number, number][] = [
    [33, 47],
    [58, 64],
    [91, 96],
    [123, 126],
  ];

  const symbols = ranges.flatMap(([a, b]) =>
    Array.from({ length: b - a + 1 }, (_, i) => String.fromCharCode(a + i))
  );

  return {
    upper,
    lower,
    nums,
    symbols,
  };
};

export const genIdx = (n: number): number => {
  const MAX = 2 ** 32;

  const limit = MAX - (MAX % n);
  const buf = new Uint32Array(1);

  let v: number;
  do {
    crypto.getRandomValues(buf);
    v = buf[0];
  } while (v >= limit);

  return v % n;
};

export const shuffle = (arg: string): string => {
  const arr = arg.split("");

  let i = arg.length - 1;

  while (i > 0) {
    const j = genIdx(i + 1);
    [arr[i], arr[j]] = [arr[j], arr[i]];
    i--;
  }

  return arr.join("");
};

export const genPwd = () => {
  const { upper, lower, nums, symbols } = genASCI();
  let pwd = "";

  for (const r of [lower, upper, nums, symbols]) {
    let j = 0;
    while (j < 4) {
      const idx = genIdx(r.length);
      pwd += r[idx];

      j++;
    }
  }

  return shuffle(pwd);
};
