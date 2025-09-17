import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        w__0: "#f5f5f5",
      },
      borderWidth: {
        3: "3px",
      },
    },
  },
  plugins: [],
};

export default config;
