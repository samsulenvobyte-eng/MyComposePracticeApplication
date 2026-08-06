## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-10 - [Compose Performance: Particle System State]
**Learning:** Using `mutableStateListOf` for high-frequency particle system updates (60+ FPS) introduces significant overhead because every modification triggers Compose's snapshot system logic.
**Action:** Use a standard `ArrayList` for particle storage and trigger `Canvas` redraws using a single `Long` state (e.g., `timeNanos`) that is updated every frame via `withFrameNanos`.
