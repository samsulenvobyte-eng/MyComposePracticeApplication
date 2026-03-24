## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-24 - [Compose Animation Optimization: Deferring State Reads]
**Learning:** Reading animation state (e.g., `Animatable.value`) directly in a high-level Composable causes the entire subtree to recompose on every animation frame (60-120 FPS). This is a major performance bottleneck for complex UI.
**Action:** Always wrap animation state reads in lambda providers (e.g., `() -> Float`) and pass them to child components. Read the value only inside `graphicsLayer { ... }` blocks or `Canvas` draw blocks to keep updates in the draw phase and avoid recompositions.
