## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose: Deferring State Reads for Animations]
**Learning:** Reading animation state (like `Animatable.value`) directly in a composable's body causes that composable and its children to recompose on every animation frame (60-120 FPS). This is extremely heavy for complex parent screens.
**Action:** Defer state reads to the narrowest possible scope. Use lambda providers (e.g., `count: () -> Int`) for components, and read animation state inside `graphicsLayer { ... }` or `Canvas { ... }` to skip Recomposition and Layout phases entirely, moving the work to the Draw phase (GPU).
