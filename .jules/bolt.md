## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Deferring State Reads to Draw Phase]
**Learning:** Using property delegation (`by`) for animation states (like `Animatable` or `infiniteTransition`) in the composition scope causes the entire Composable (and its children) to recompose on every frame (~60-120 FPS). This is especially wasteful for infinite animations like breathing or rotation that don't affect layout.
**Action:** Access animation `.value` directly inside "deferring" blocks like `Canvas`, `graphicsLayer { ... }`, or `Modifier.drawBehind { ... }`. Pass high-frequency states to child Composables using lambda providers `() -> T` instead of raw values.
