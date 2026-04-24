## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-04-24 - [Compose Animation: Deferring State Reads]
**Learning:** Reading high-frequency animation states (like `Animatable.value` or `infiniteRepeatable` progress) inside a Composable's body causes it to recompose every frame.
**Action:** Always pass animation states as lambda providers (e.g., `() -> Float`) to child components and read them inside `Canvas` draw blocks or `graphicsLayer` modifiers to defer state reading to the draw phase and skip recomposition.
