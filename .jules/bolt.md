## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Animation: Deferring State Reads]
**Learning:** Reading animation state (e.g., `animateFloatAsState().value`) during the composition phase triggers full recomposition of the composable every frame. This is extremely expensive for high-frequency animations.
**Action:** Defer state reads to the Draw phase by using `Modifier.graphicsLayer { ... }` or passing lambda providers `() -> T` to sub-composables. This allows the UI to skip Recomposition and Layout phases entirely during animation frames.
