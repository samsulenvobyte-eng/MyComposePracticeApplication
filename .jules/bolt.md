## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Jetpack Compose: Deferring State Reads]
**Learning:** High-frequency animations (e.g., bar charts, counters) trigger expensive recomposition cycles if state is read directly in the composable body.
**Action:** Use lambda providers (e.g., `progress: () -> Float`) for animated values and read them only inside `Canvas` draw blocks or `graphicsLayer` modifiers. This defers the state read to the draw phase, bypassing recomposition and layout entirely. Replacing conditional composition (`if (visible)`) with animated `alpha` in `graphicsLayer` further stabilizes the composition tree.
