## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Animation: Deferred State Reading]
**Learning:** Reading animation state (like `Animatable.value`) directly in the composition body causes the component to recompose every frame. This is expensive for complex UIs.
**Action:** Pass state as lambda providers (e.g., `() -> Float`) and read them only inside `Canvas` draw blocks or `graphicsLayer` modifiers. This defers state reading to the draw phase, bypassing unnecessary composition and layout.
