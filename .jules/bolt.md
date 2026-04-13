## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-04-13 - [Compose Animation: Phase-Based Optimization]
**Learning:** Using standard modifiers like `.scale()` or `.alpha()` with high-frequency animation states (e.g., `Animatable.value`) triggers expensive recomposition of the parent composable on every frame.
**Action:** Always use block-based modifiers like `.graphicsLayer { scaleX = ... }` or `.drawBehind { ... }` to read animation state. This defers state reading to the draw phase, skipping recomposition and layout entirely.
