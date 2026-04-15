## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Animation Performance: Deferring State Reads]
**Learning:** Reading high-frequency animation state (like `Animatable.value`) directly in the composition phase causes the entire component to recompose every frame.
**Action:** Always pass animation state as lambda providers (`() -> T`) to child components and read the value only where needed (e.g., inside `Canvas` draw blocks or `graphicsLayer` modifiers) to defer state reading to the draw phase and avoid unnecessary recompositions. Wrap derived integer values from float animations in `derivedStateOf` to further limit updates.
