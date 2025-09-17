import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  webpack(config, options) {
    if (options.isServer) {
      config.watchOptions = {
        ignored: [
          "**/tests/**",
          "**/test-results/**",
          "**/playwright-report/**",
        ],
      };
    }
    return config;
  },

  turbopack: {
    resolveAlias: { "@": "./src" },
  },
};

export default nextConfig;
