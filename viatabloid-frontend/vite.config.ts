import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react-swc";

export default defineConfig({
  plugins: [react()],
  base: "/",
  build: {
    outDir: "dist",
    emptyOutDir: true,
  },
  test: {
    globals: true,
    environment: "jsdom",

    exclude: ["**/node_modules/**", "**/dist/**"], // Default exclusions
    setupFiles: "./src/setupTests.ts", // Add this line
    coverage: {
      provider: "v8",
    },
  },
});
