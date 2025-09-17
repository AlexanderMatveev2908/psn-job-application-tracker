/** @type {import('@svgr/core').Config} */
module.exports = {
  typescript: true,
  icon: true,
  expandProps: "end",
  exportType: "default",
  dimensions: false,
  svgProps: {
    "aria-hidden": "true",
    stroke: "currentColor",
  },
  svgo: true,
  svgoConfig: {
    plugins: [
      { name: "removeEditorsNSData", active: true },
      { name: "removeAttrs", params: { attrs: ["fill", "stroke"] } },
      { name: "inlineStyles", params: { onlyMatchedOnce: false } },
      { name: "removeStyleElement", active: true },
      { name: "removeEmptyContainers", active: true },
      { name: "removeUselessDefs", active: true },
      { name: "convertColors", active: true },
      { name: "cleanupAttrs", active: true },
    ],
  },
};
