## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-17 - [Compose Performance: Deferring State Reads]
**Learning:** Passing state to Composable functions as raw values (e.g., `Float`) causes the entire function and its children to recompose every time the value changes (e.g., 60+ times per second during animations).
**Action:** Pass state as lambda providers (e.g., `() -> Float`) and read the value only inside `graphicsLayer` blocks or `Canvas` draw blocks. This defers state reading to the Draw phase, skipping the Recomposition and Layout phases entirely for those updates.
